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

public abstract class SteamFluid extends StarFluid {
    public SteamFluid() {
        super(Direction.UP);
    }

    @Override
    public Fluid getFlowing() {
        return MaldingFluids.STEAM.flowing();
    }

    @Override
    public Fluid getStill() {
        return MaldingFluids.STEAM.still();
    }

    @Override
    public Item getBucketItem() {
        return MaldingFluids.STEAM.bucket();
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return MaldingFluids.STEAM.block().getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
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
