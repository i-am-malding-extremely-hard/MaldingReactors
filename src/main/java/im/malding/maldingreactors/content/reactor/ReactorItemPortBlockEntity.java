package im.malding.maldingreactors.content.reactor;

import im.malding.maldingreactors.content.MaldingBlockEntities;
import im.malding.maldingreactors.content.MaldingFluids;
import im.malding.maldingreactors.content.MaldingItems;
import im.malding.maldingreactors.content.handlers.ReactorItemPortScreenHandler;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.endec.BuiltInEndecs;
import io.wispforest.owo.serialization.endec.KeyedEndec;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorItemPortBlockEntity extends ReactorBaseBlockEntity implements NamedScreenHandlerFactory, Tickable {

    public static final KeyedEndec<ItemStack> FUEL_SLOT_KEY = BuiltInEndecs.ITEM_STACK.keyed("FuelSlot", ItemStack.EMPTY);
    public static final KeyedEndec<ItemStack> WASTE_SLOT_KEY = BuiltInEndecs.ITEM_STACK.keyed("WasteSlot", ItemStack.EMPTY);

    public static final KeyedEndec<Boolean> AUTO_EXPORT = Endec.BOOLEAN.keyed("AutoExportWaste", false);

    public final SimpleInventory fuelSlot = new SimpleInventory(1);
    public final SimpleInventory wasteSlot = new SimpleInventory(1);

    public boolean autoExportWaste = false;

    public ReactorItemPortBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_ITEM_PORT, pos, state);

        fuelSlot.addListener(sender -> this.markDirty());
        wasteSlot.addListener(sender -> this.markDirty());
    }

    public ReactorItemPortBlockEntity autoExportWaste(boolean value){
        this.autoExportWaste = value;
        this.markDirty();

        return this;
    }

    @Override
    public void serverTick() {
        if(autoExportWaste && this.getWorld().getTime() % 2 == 0){
            attemptWasteExport();
        }
    }

    public void attemptWasteExport(){
        var controller = this.getController();

        if(controller == null || controller.getWasteTank().amount < FluidConstants.INGOT) return;

        var currentStack = wasteSlot.getStack(0);

        if(currentStack.getCount() >= 64) return;

        var wasteCount = (int) Math.floor(controller.getWasteTank().amount / (float) FluidConstants.INGOT);

        var emptySpaceAmount = 64 - currentStack.getCount();

        int consumedCount;

        if(emptySpaceAmount >= wasteCount){
            var wasteStack = new ItemStack(MaldingItems.MALDING_COPIUM_INGOT);

            wasteStack.setCount(currentStack.getCount() + wasteCount);

            wasteSlot.setStack(0, wasteStack);

            consumedCount = wasteCount;
        } else {
            var wasteStack = new ItemStack(MaldingItems.MALDING_COPIUM_INGOT);

            wasteStack.setCount(currentStack.getCount() + emptySpaceAmount);

            wasteSlot.setStack(0, wasteStack);

            consumedCount = emptySpaceAmount;
        }

        try(var transaction = Transaction.openOuter()){
            controller.getWasteTank()
                    .extract(FluidVariant.of(MaldingFluids.MALDING_COPIUM.still()), consumedCount * controller.getWasteTank().amount, transaction);

            transaction.commit();
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        fuelSlot.setStack(0, nbt.get(FUEL_SLOT_KEY));
        wasteSlot.setStack(0, nbt.get(WASTE_SLOT_KEY));

        this.autoExportWaste = nbt.get(AUTO_EXPORT);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.put(FUEL_SLOT_KEY, fuelSlot.getStack(0));
        nbt.put(WASTE_SLOT_KEY, wasteSlot.getStack(0));

        nbt.put(AUTO_EXPORT, this.autoExportWaste);
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Reactor Port");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf openingData = PacketByteBufs.create();
        openingData.writeBlockPos(pos);
        return new ReactorItemPortScreenHandler(syncId, ScreenHandlerContext.create(this.world, this.pos), inv, openingData, this.fuelSlot, this.wasteSlot);
    }
}
