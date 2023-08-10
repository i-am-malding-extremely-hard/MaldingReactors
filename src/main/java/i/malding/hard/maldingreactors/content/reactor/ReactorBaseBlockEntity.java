package i.malding.hard.maldingreactors.content.reactor;

import io.wispforest.owo.ops.WorldOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorBaseBlockEntity extends BlockEntity {

    public BlockPos controllerPos = null;

    public static final String CONTROLLER_POS_KEY = "ControllerPos";

    public ReactorBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    public ReactorControllerBlockEntity getController() {
        if (isFullMultipartStructure()) {
            return (ReactorControllerBlockEntity) this.getWorld().getBlockEntity(this.getControllerPos());
        }

        return null;
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
    }

    @Nullable
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public void clientTick() {}

    public void serverTick() {}

    public void onRemoval(BlockPos pos) {}

    @Override
    public void readNbt(NbtCompound nbt) {
        this.setControllerPos(NbtHelper.toBlockPos(nbt.getCompound(CONTROLLER_POS_KEY)));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (isFullMultipartStructure()) {
            nbt.put(CONTROLLER_POS_KEY, NbtHelper.fromBlockPos(this.getControllerPos()));
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void markDirty() {
        WorldOps.updateIfOnServer(this.world, this.pos);

        super.markDirty();
    }

    public boolean isFullMultipartStructure() {
        return this.getControllerPos() != null;
    }
}
