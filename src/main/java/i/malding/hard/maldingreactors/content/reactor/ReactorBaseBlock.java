package i.malding.hard.maldingreactors.content.reactor;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ReactorBaseBlock extends BlockWithEntity {

    public ReactorBaseBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock() && state.hasBlockEntity()) {
            blockEntityRemoval(world, pos);
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ((world1, pos, state1, blockEntity) -> {
            if (world1.isClient) {
                ((ReactorBaseBlockEntity) blockEntity).clientTick();
            } else {
                ((ReactorBaseBlockEntity) blockEntity).serverTick();
            }
        });
    }

    public void blockEntityRemoval(World world, BlockPos pos) {
        ((ReactorBaseBlockEntity) world.getBlockEntity(pos)).onRemoval(pos);
    }
}
