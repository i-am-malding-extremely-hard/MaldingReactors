package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReactorFuelRodBlockEntity extends ReactorBaseBlockEntity {

    private BlockPos rodControllerPos;

    public ReactorFuelRodBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_FUEL_ROD, pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {

    }

    public void setRodControllerPos(BlockPos pos){
        this.rodControllerPos = pos;
    }

    public BlockPos getRodControllerPos(){
        return this.rodControllerPos;
    }

}
