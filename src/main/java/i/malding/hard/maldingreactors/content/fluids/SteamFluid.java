package i.malding.hard.maldingreactors.content.fluids;

import i.malding.hard.maldingreactors.content.AllFluids;
import me.alphamode.star.world.fluids.StarFluid;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public abstract class SteamFluid extends StarFluid {
    public SteamFluid() {
        super(Direction.UP);
    }

    @Override
    public Fluid getFlowing() {
        return AllFluids.STEAM.flowing();
    }

    @Override
    public Fluid getStill() {
        return AllFluids.STEAM.still();
    }

    @Override
    public Item getBucketItem() {
        return AllFluids.STEAM.bucket();
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return AllFluids.STEAM.block().getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    public static class Flowing extends SteamFluid {
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

    public static class Still extends SteamFluid {
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
