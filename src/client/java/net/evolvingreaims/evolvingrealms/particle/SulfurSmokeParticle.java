package net.evolvingreaims.evolvingrealms.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

/**
 * Sulfur Smoke — thick yellowish smoke rising from sulfur vents.
 */
@Environment(EnvType.CLIENT)
public class SulfurSmokeParticle extends SpriteBillboardParticle {

    private SulfurSmokeParticle(ClientWorld world, double x, double y, double z,
                                 double vx, double vy, double vz, SpriteProvider sprites) {
        super(world, x, y, z);
        this.setSprite(sprites);
        this.maxAge = 30 + random.nextInt(20);
        this.scale = 0.5f + random.nextFloat() * 0.5f;
        this.velocityX = vx + (random.nextDouble() - 0.5) * 0.05;
        this.velocityY = 0.06 + random.nextDouble() * 0.02;
        this.velocityZ = vz + (random.nextDouble() - 0.5) * 0.05;
        this.red   = 0.85f;
        this.green = 0.82f;
        this.blue  = 0.20f;
        this.alpha = 0.7f;
        this.gravityStrength = -0.01f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha *= 0.96f;
        this.scale += 0.02f;
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
            return new SulfurSmokeParticle(world, x, y, z, vx, vy, vz, sprites);
        }
    }
}
