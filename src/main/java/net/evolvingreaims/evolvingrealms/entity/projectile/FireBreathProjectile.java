package net.evolvingreaims.evolvingrealms.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * Fire Breath Projectile — used by the Ancient Sulfur Titan's Phase 1 fire
 * breath ability. Deals 8 damage on direct hit and sets all entities within
 * 2.5 blocks on fire for 6 seconds.
 */
public class FireBreathProjectile extends ThrownItemEntity {

    public FireBreathProjectile(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    public FireBreathProjectile(World world, LivingEntity owner) {
        super(EntityType.FIREBALL, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY(), owner.getZ());
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FIRE_CHARGE;
    }

    @Override
    protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);
        applyFireEffect();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        applyFireEffect();
        if (!this.getWorld().isClient) {
            ServerWorld sw = (ServerWorld) this.getWorld();
            sw.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 2, 0.5, 0.5, 0.5, 0.0);
            this.discard();
        }
    }

    private void applyFireEffect() {
        if (!this.getWorld().isClient) {
            ServerWorld sw = (ServerWorld) this.getWorld();
            List<LivingEntity> nearby = sw.getEntitiesByClass(LivingEntity.class,
                    this.getBoundingBox().expand(2.5),
                    e -> e != this.getOwner());

            for (LivingEntity e : nearby) {
                e.damage(sw, sw.getDamageSources().magic(), 8.0f);
                e.setOnFireFor(6);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(),
                    this.getVelocity().x * 0.1, this.getVelocity().y * 0.1, this.getVelocity().z * 0.1);
            this.getWorld().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0.05, 0);
        }
    }
}
