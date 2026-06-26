package net.evolvingreaims.evolvingrealms.item;

import net.evolvingreaims.evolvingrealms.registry.ModItems;
import net.evolvingreaims.evolvingrealms.registry.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

public class AcidFlaskProjectile extends ThrownItemEntity {

    public AcidFlaskProjectile(World world, LivingEntity owner, ItemStack stack) {
        super(EntityType.SNOWBALL, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    public AcidFlaskProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ACID_FLASK;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();

            // Splash area damage and poison
            List<LivingEntity> entities = serverWorld.getEntitiesByClass(
                    LivingEntity.class,
                    this.getBoundingBox().expand(3.0),
                    e -> e != this.getOwner());

            for (LivingEntity entity : entities) {
                double dist = entity.distanceTo(this);
                if (dist <= 3.0) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 2));
                    entity.damage(serverWorld, serverWorld.getDamageSources().magic(), 4.0f);
                }
            }

            // Particles
            serverWorld.spawnParticles(ParticleTypes.SPLASH, this.getX(), this.getY(), this.getZ(),
                    30, 0.5, 0.1, 0.5, 0.2);
            serverWorld.spawnParticles(ParticleTypes.EFFECT, this.getX(), this.getY(), this.getZ(),
                    15, 0.3, 0.3, 0.3, 0.1);

            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.EFFECT,
                    this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }
}
