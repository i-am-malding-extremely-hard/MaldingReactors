package i.malding.hard.maldingreactors.content.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class ReactorComponentBlockEntity extends BlockEntity {

    public BlockPos controllerPos = null;

    public static final String CONTROLLER_POS_KEY = "ControllerPos";

    public ReactorComponentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
    }

    @Nullable
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.setControllerPos(NbtHelper.toBlockPos(nbt.getCompound(CONTROLLER_POS_KEY)));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if(isFullMultipartStructure()) {
            nbt.put(CONTROLLER_POS_KEY, NbtHelper.fromBlockPos(this.getControllerPos()));
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public boolean isFullMultipartStructure(){
        return this.getControllerPos() != null;
    }
}
