package i.malding.hard.maldingreactors.content.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorFuelRodBlock extends ReactorBaseBlock {

    public static final BooleanProperty HAS_FUEL = BooleanProperty.of("has_fuel");

    public ReactorFuelRodBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.getDefaultState().with(HAS_FUEL, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorFuelRodBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_FUEL);
    }

}
