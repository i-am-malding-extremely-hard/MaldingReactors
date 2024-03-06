package im.malding.maldingreactors.content.reactor;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class ReactorBaseBlock extends BlockWithEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final BlockEntityFactory factory;
    private final BlockRenderType renderType;

    public ReactorBaseBlock(Settings settings, BlockEntityFactory factory) {
        this(settings, factory, BlockRenderType.MODEL);
    }

    public ReactorBaseBlock(Settings settings, BlockEntityFactory factory, BlockRenderType type) {
        super(settings);

        this.renderType = type;
        this.factory = factory;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return renderType;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return this.factory.createBlockEntity(pos, state);
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
