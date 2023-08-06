package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ReactorFuelRodBlockEntity extends ReactorBaseBlockEntity {

    private static final Long maxHeatAllowed = 1000000L;
    private static final String STORED_HEAT_KEY = "CurrentHeatLevel";

    private Long currentHeatStored = 0L;

    @Nullable
    public BlockPos rodControllerPos = null;

    public ReactorFuelRodBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD, pos, state);
    }

    public void setRodControllerPos(BlockPos pos){
        this.rodControllerPos = pos;
    }

    @Override
    public void serverTick() {}

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if(nbt.contains("RodControllerPos")) {
            this.rodControllerPos = NbtHelper.toBlockPos(nbt);
        }

        this.currentHeatStored = nbt.getLong(STORED_HEAT_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if(rodControllerPos != null){
            nbt.put("RodControllerPos", NbtHelper.fromBlockPos(this.rodControllerPos));
        }

        nbt.putLong(STORED_HEAT_KEY, currentHeatStored);
    }

}
