package i.malding.hard.maldingreactors.content.reactor;

import com.mojang.logging.LogUtils;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;

public abstract class ReactorBaseBlock extends BlockWithEntity {

    private static Logger LOGGER = LogUtils.getLogger();

    public ReactorBaseBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock() && state.hasBlockEntity()) blockEntityRemoval(world, pos);

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    public void blockEntityRemoval(World world, BlockPos pos) {
        if(!(world.getBlockEntity(pos) instanceof ReactorBaseBlockEntity blockEntity)){
            LOGGER.error("A given block at the following position [{}] was found not be a ReactorBaseBlockEntity which may be a issue.", pos);

            return;
        }

        blockEntity.onRemoval(pos);
    }
}
