package net.evolvingreaims.evolvingrealms.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

/**
 * Lava Ember — bright orange-red sparks ejected during volcano eruptions
 * and around the Lava Worm entity.
 */
@Environment(EnvType.CLIENT)
public class LavaEmberParticle extends SpriteBillboardParticle {

    private LavaEmberParticle(ClientWorld world, double x, double y, double z,
                               double vx, double vy, double vz, SpriteProvider sprites) {
        super(world, x, y, z);
        this.setSprite(sprites);
        this.maxAge = 20 + random.nextInt(15);
        this.scale = 0.06f + random.nextFloat() * 0.08f;
        this.velocityX = vx + (random.nextDouble() - 0.5) * 0.2;
        this.velocityY = vy + 0.15 + random.nextDouble() * 0.1;
        this.velocityZ = vz + (random.nextDouble() - 0.5) * 0.2;
        this.red   = 1.0f;
        this.green = 0.35f + random.nextFloat() * 0.15f;
        this.blue  = 0.0f;
        this.alpha = 1.0f;
        this.gravityStrength = 0.08f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        // Cool down colour over lifetime
        float lifeFraction = (float) this.age / this.maxAge;
        this.green = Math.max(0, 0.35f - lifeFraction * 0.35f);
        this.alpha = Math.max(0, 1.0f - lifeFraction * lifeFraction);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle create(SimpleParticleType type, ClientWorld world,
                               double x, double y, double z,
                               double vx, double vy, double vz) {
            return new LavaEmberParticle(world, x, y, z, vx, vy, vz, sprites);
        }
    }
}
