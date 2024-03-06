package im.malding.maldingreactors.content.fluids;

import me.alphamode.star.world.fluids.StarFluid;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Direction;

public abstract class MaldingFluid extends StarFluid {
    public MaldingFluid() {
        super(Direction.DOWN);
    }

    @Override
    public ParticleEffect getBubbleParticle(Entity entity) {
        return ParticleTypes.BUBBLE;
    }

    @Override
    public ParticleEffect getSplashParticle(Entity entity) {
        return ParticleTypes.SPLASH;
    }
}
