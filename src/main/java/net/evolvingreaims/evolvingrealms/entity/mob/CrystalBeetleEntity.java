package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Crystal Beetle — a small but hard-shelled insect mob found in crystal caves.
 *
 * Unique mechanics:
 *   - Shell defence: while not moving, takes only 30 % damage (shield stance)
 *   - Curl attack: charges forward fast for 2 seconds, dealing damage to everything in path
 *   - Crystal dust passive: nearby players get Mining Fatigue I within 4 blocks
 *   - Group strength: 10 % attack bonus per other Crystal Beetle within 8 blocks (max +50 %)
 */
public class CrystalBeetleEntity extends HostileEntity {

    private int    chargeCooldown = 0;
    private boolean charging      = false;
    private int    chargeTimer    = 0;

    public CrystalBeetleEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,     22.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.24)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,  5.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.4)
                .add(EntityAttributes.GENERIC_ARMOR,          8.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,   20.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient()) {
            // Crystal dust debuff on nearby players
            if (this.age % 20 == 0) {
                this.getWorld().getEntitiesByClass(PlayerEntity.class,
                        this.getBoundingBox().expand(4.0),
                        net.minecraft.entity.EntityPredicates.VALID_ENTITY)
                    .forEach(p -> p.addStatusEffect(
                            new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 40, 0)));
            }

            // Charge attack
            if (chargeCooldown > 0) chargeCooldown--;
            if (!charging && chargeCooldown == 0 && this.getTarget() != null
                    && this.squaredDistanceTo(this.getTarget()) < 100) {
                startCharge();
            }
            if (charging) {
                chargeTimer++;
                if (chargeTimer >= 40) {
                    charging   = false;
                    chargeTimer = 0;
                    this.setNoGravity(false);
                    // Reset speed
                    this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(0.24);
                }
            }
        } else {
            // Crystal sparkle particles
            if (random.nextInt(6) == 0) {
                this.getWorld().addParticle(ParticleTypes.END_ROD,
                        this.getX() + (random.nextDouble() - 0.5),
                        this.getY() + 0.3,
                        this.getZ() + (random.nextDouble() - 0.5),
                        0, 0, 0);
            }
        }
    }

    private void startCharge() {
        if (this.getTarget() == null) return;
        charging      = true;
        chargeTimer   = 0;
        chargeCooldown = 120;
        // Triple movement speed
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.72);
        // Charge in the direction of target
        net.minecraft.util.math.Vec3d dir = this.getTarget().getPos().subtract(this.getPos()).normalize();
        this.setVelocity(dir.multiply(1.5));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // Shell defence when stationary
        boolean stationary = this.getVelocity().horizontalLengthSquared() < 0.01;
        if (stationary && !source.isIn(net.minecraft.registry.tag.DamageTypeTags.IS_EXPLOSION)) {
            amount *= 0.3f;
        }
        return super.damage(source, amount);
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        // Group strength bonus
        long nearbyBeetles = this.getWorld().getEntitiesByClass(CrystalBeetleEntity.class,
                this.getBoundingBox().expand(8.0), b -> b != this).size();
        float bonus = 1.0f + Math.min(nearbyBeetles * 0.1f, 0.5f);
        float baseDamage = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (target instanceof LivingEntity living) {
            living.damage(this.getDamageSources().mobAttack(this), baseDamage * bonus);
            return true;
        }
        return super.tryAttack(target);
    }

    @Override protected SoundEvent getAmbientSound()              { return null; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.CRYSTAL_GOLEM_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.CRYSTAL_GOLEM_SHATTER; }
}
