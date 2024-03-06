package im.malding.maldingreactors.content.fluids;

import im.malding.maldingreactors.content.MaldingFluids;
import me.alphamode.star.world.fluids.StarFluid;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public abstract class CoolantFluid extends StarFluid {
    public CoolantFluid() {
        super(Direction.UP);
    }

    @Override
    public Fluid getFlowing() {
        return MaldingFluids.COOLANT.flowing();
    }

    @Override
    public Fluid getStill() {
        return MaldingFluids.COOLANT.still();
    }

    @Override
    public Item getBucketItem() {
        return MaldingFluids.COOLANT.bucket();
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return MaldingFluids.COOLANT.block().getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    //--

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {

    }

    @Override
    public ParticleEffect getBubbleParticle(Entity entity) {
        return ParticleTypes.BUBBLE;
    }

    @Override
    public ParticleEffect getSplashParticle(Entity entity) {
        return ParticleTypes.SPLASH;
    }

    //--

    public static class Flowing extends CoolantFluid {
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

    public static class Still extends CoolantFluid {
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
