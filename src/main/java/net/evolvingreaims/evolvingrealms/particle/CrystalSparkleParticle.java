package net.evolvingreaims.evolvingrealms.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

/**
 * Crystal Sparkle — bright cyan/white glitter emitted by crystal blocks and golems.
 */
@Environment(EnvType.CLIENT)
public class CrystalSparkleParticle extends SpriteBillboardParticle {

    private CrystalSparkleParticle(ClientWorld world, double x, double y, double z,
                                    double vx, double vy, double vz, SpriteProvider sprites) {
        super(world, x, y, z);
        this.setSprite(sprites);
        this.maxAge = 12 + random.nextInt(8);
        this.scale = 0.08f + random.nextFloat() * 0.06f;
        this.velocityX = vx + (random.nextDouble() - 0.5) * 0.1;
        this.velocityY = vy + random.nextDouble() * 0.05;
        this.velocityZ = vz + (random.nextDouble() - 0.5) * 0.1;
        this.red   = 0.7f + random.nextFloat() * 0.3f;
        this.green = 0.9f;
        this.blue  = 1.0f;
        this.alpha = 1.0f;
        this.gravityStrength = 0.02f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha -= 0.06f;
        if (this.alpha <= 0) this.markDead();
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
            return new CrystalSparkleParticle(world, x, y, z, vx, vy, vz, sprites);
        }
    }
}
