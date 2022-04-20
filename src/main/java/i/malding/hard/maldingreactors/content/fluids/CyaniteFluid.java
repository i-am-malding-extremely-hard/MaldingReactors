package i.malding.hard.maldingreactors.content.fluids;

import i.malding.hard.maldingreactors.content.AllFluids;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

public abstract class CyaniteFluid extends MaldingFluid {
    @Override
    public Fluid getFlowing() {
        return AllFluids.CYANITE.flowing();
    }

    @Override
    public Fluid getStill() {
        return AllFluids.CYANITE.still();
    }

    @Override
    public Item getBucketItem() {
        return AllFluids.CYANITE.bucket();
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return AllFluids.CYANITE.block().getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    public static class Flowing extends CyaniteFluid {
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

    public static class Still extends CyaniteFluid {
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