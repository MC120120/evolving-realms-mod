package net.evolvingreaims.evolvingrealms.entity.projectile;

import net.evolvingreaims.evolvingrealms.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

/**
 * Projectile fired by AcidSpiderEntity. Deals 3 damage + Poison II for 5s
 * and Slowness I for 3s on hit.
 */
public class AcidSpitProjectile extends ThrownItemEntity {

    public AcidSpitProjectile(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    public AcidSpitProjectile(World world, LivingEntity owner) {
        super(EntityType.SNOWBALL, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);
        if (hit.getEntity() instanceof LivingEntity target) {
            if (!this.getWorld().isClient) {
                target.damage((ServerWorld) this.getWorld(),
                        this.getWorld().getDamageSources().magic(), 3.0f);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0));
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            ServerWorld sw = (ServerWorld) this.getWorld();
            sw.spawnParticles(ParticleTypes.SPLASH, getX(), getY(), getZ(), 20, 0.3, 0.1, 0.3, 0.1);
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.EFFECT, getX(), getY(), getZ(), 0, 0, 0);
        }
    }
}
