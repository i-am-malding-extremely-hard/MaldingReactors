package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.content.handlers.ReactorItemPortScreenHandler;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorItemPortBlockEntity extends ReactorBaseBlockEntity implements NamedScreenHandlerFactory {

    public static final NbtKey<ItemStack> FUEL_SLOT_KEY = new NbtKey<>("FuelSlot", NbtKey.Type.ITEM_STACK);
    public static final NbtKey<ItemStack> WASTE_SLOT_KEY = new NbtKey<>("WasteSlot", NbtKey.Type.ITEM_STACK);

    public final SimpleInventory fuelSlot = new SimpleInventory(1);
    public final SimpleInventory wasteSlot = new SimpleInventory(1);

    public ReactorItemPortBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_ITEM_PORT, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        fuelSlot.setStack(0, nbt.get(FUEL_SLOT_KEY));
        wasteSlot.setStack(0, nbt.get(WASTE_SLOT_KEY));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.put(FUEL_SLOT_KEY, fuelSlot.getStack(0));
        nbt.put(WASTE_SLOT_KEY, wasteSlot.getStack(0));
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Reactor Port");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ReactorItemPortScreenHandler(syncId, inv, ScreenHandlerContext.create(this.world, this.pos), this.fuelSlot, this.wasteSlot);
    }
}
