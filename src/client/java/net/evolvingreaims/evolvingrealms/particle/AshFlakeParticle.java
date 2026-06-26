package net.evolvingreaims.evolvingrealms.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

/**
 * Ash Flake — dark grey flakes drifting down from ash-falls and wildfire events.
 */
@Environment(EnvType.CLIENT)
public class AshFlakeParticle extends SpriteBillboardParticle {

    private AshFlakeParticle(ClientWorld world, double x, double y, double z,
                              double vx, double vy, double vz, SpriteProvider sprites) {
        super(world, x, y, z);
        this.setSprite(sprites);
        this.maxAge = 60 + random.nextInt(40);
        this.scale = 0.1f + random.nextFloat() * 0.12f;
        this.velocityX = vx + (random.nextDouble() - 0.5) * 0.04;
        this.velocityY = -0.02 - random.nextDouble() * 0.01;
        this.velocityZ = vz + (random.nextDouble() - 0.5) * 0.04;
        this.red   = 0.25f;
        this.green = 0.22f;
        this.blue  = 0.20f;
        this.alpha = 0.8f;
        this.gravityStrength = 0.015f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        // Gentle drift
        this.velocityX += (random.nextDouble() - 0.5) * 0.002;
        this.velocityZ += (random.nextDouble() - 0.5) * 0.002;
        super.tick();

        if (this.age > this.maxAge - 15) {
            this.alpha *= 0.92f;
        }
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
            return new AshFlakeParticle(world, x, y, z, vx, vy, vz, sprites);
        }
    }
}
