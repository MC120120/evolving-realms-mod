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

/**
 * Crystal Spike — fired by CrystalGolemEntity. Deals 6 piercing damage and
 * briefly blinds the target (Blindness I, 2s). Leaves a crystal shard
 * particle trail.
 */
public class CrystalSpikeEntity extends ThrownItemEntity {

    public CrystalSpikeEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    public CrystalSpikeEntity(World world, LivingEntity owner) {
        super(EntityType.SNOWBALL, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected Item getDefaultItem() {
        return Items.QUARTZ;
    }

    @Override
    protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);
        if (hit.getEntity() instanceof LivingEntity target && !this.getWorld().isClient) {
            target.damage((ServerWorld) this.getWorld(),
                    this.getWorld().getDamageSources().magic(), 6.0f);
            target.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.BLINDNESS, 40, 0));
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            ServerWorld sw = (ServerWorld) this.getWorld();
            sw.spawnParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 16, 0.4, 0.4, 0.4, 0.05);
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, 0, 0);
        }
    }
}
