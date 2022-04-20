package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ReactorFuelRodBlockEntity extends ReactorComponentBlockEntity {
    public ReactorFuelRodBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD, pos, state);
    }

    public void tick() {

    }
}
