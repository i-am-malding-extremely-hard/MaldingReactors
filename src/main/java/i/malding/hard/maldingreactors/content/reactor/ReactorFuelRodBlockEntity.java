package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReactorFuelRodBlockEntity extends ReactorComponentBlockEntity {
    public ReactorFuelRodBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {

    }
}
