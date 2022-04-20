package i.malding.hard.maldingreactors.content.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ReactorFuelRodController extends BlockWithEntity {

    protected ReactorFuelRodController(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorFuelRodControllerBlockEntity(pos, state);
    }
}
