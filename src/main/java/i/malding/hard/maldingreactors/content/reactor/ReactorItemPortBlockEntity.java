package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.handlers.ReactorItemPortScreenHandler;
import i.malding.hard.maldingreactors.content.AllBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class ReactorItemPortBlockEntity extends ReactorComponentBlockEntity implements NamedScreenHandlerFactory {

    public static final String FUEL_SLOT_KEY = "FuelSlot";
    public static final String WASTE_SLOT_KEY = "WasteSlot";

    public SimpleInventory fuelSlot;
    public SimpleInventory wasteSlot;

    public ReactorItemPortBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.REACTOR_ITEM_PORT, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
       super.writeNbt(nbt);
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
