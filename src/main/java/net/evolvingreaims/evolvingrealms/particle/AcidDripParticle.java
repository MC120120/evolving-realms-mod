package net.evolvingreaims.evolvingrealms.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

/**
 * Acid Drip — bright green corrosive droplets falling from acid-stone ceilings.
 */
@Environment(EnvType.CLIENT)
public class AcidDripParticle extends SpriteBillboardParticle {

    private AcidDripParticle(ClientWorld world, double x, double y, double z,
                              SpriteProvider sprites) {
        super(world, x, y, z);
        this.setSprite(sprites);
        this.maxAge = 20 + random.nextInt(10);
        this.scale = 0.15f + random.nextFloat() * 0.1f;
        this.velocityX = 0;
        this.velocityY = -0.12 - random.nextDouble() * 0.04;
        this.velocityZ = 0;
        this.red   = 0.15f;
        this.green = 0.85f;
        this.blue  = 0.10f;
        this.alpha = 0.9f;
        this.gravityStrength = 0.06f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround) {
            this.velocityX = 0;
            this.velocityZ = 0;
            this.alpha *= 0.85f;
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
            return new AcidDripParticle(world, x, y, z, sprites);
        }
    }
}
