package i.malding.hard.maldingreactors.content.fluids;

import i.malding.hard.maldingreactors.content.MaldingFluids;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.world.World;

public abstract class CopiumFluid extends MaldingFluid {
    @Override
    public Fluid getFlowing() {
        return MaldingFluids.COPIUM.flowing();
    }

    @Override
    public Fluid getStill() {
        return MaldingFluids.COPIUM.still();
    }

    @Override
    public Item getBucketItem() {
        return MaldingFluids.COPIUM.bucket();
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return MaldingFluids.COPIUM.block().getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    public static class Flowing extends CopiumFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends CopiumFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
