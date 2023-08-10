package i.malding.hard.maldingreactors.content.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorPowerPortBlock extends ReactorSingleFaceBlock {

    public ReactorPowerPortBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorPowerPortBlockEntity(pos, state);
    }
}
