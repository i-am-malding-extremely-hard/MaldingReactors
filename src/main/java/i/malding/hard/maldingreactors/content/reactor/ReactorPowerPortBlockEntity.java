package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReactorPowerPortBlockEntity extends ReactorComponentBlockEntity {

    public ReactorPowerPortBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_POWER_PORT, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {

    }
}
