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
 * Sulfur Phantom — an ethereal, glowing creature that dives from above.
 *
 * Unique mechanics:
 *   - Phases in and out of visibility (3 s visible, 2 s invisible but still present)
 *   - While invisible: halved damage taken
 *   - Dive-bomb attack: charges down at high speed, deals 12 damage, lifts back up
 *   - On hit: applies Wither I for 3 seconds
 *   - Spawns only in the upper layers of the Sulfur Dimension (high Y)
 *   - Immune to fire; weak to water (takes double damage)
 */
public class SulfurPhantomEntity extends HostileEntity {

    private int phaseTimer    = 0;
    private boolean phased    = false; // true = invisible/semi-invulnerable
    private int diveCooldown  = 0;
    private boolean diving    = false;

    public SulfurPhantomEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,     40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.8)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,  12.0)
                .add(EntityAttributes.GENERIC_ARMOR,          4.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,   48.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.5, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 16.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        phaseTimer++;

        // Phase cycle: 60 ticks visible, 40 ticks phased
        if (!phased && phaseTimer >= 60) {
            phased     = true;
            phaseTimer = 0;
            this.setInvisible(true);
        } else if (phased && phaseTimer >= 40) {
            phased     = false;
            phaseTimer = 0;
            this.setInvisible(false);
        }

        if (!this.getWorld().isClient()) {
            // Dive bomb
            if (diveCooldown > 0) diveCooldown--;
            if (diveCooldown == 0 && this.getTarget() != null && !diving) {
                startDive();
            }
        } else {
            // Sulfur glow particles
            if (random.nextInt(4) == 0) {
                this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX() + (random.nextDouble() - 0.5) * 0.9,
                        this.getY() + random.nextDouble() * 0.5,
                        this.getZ() + (random.nextDouble() - 0.5) * 0.9,
                        0, -0.05, 0);
            }
        }
    }

    private void startDive() {
        if (this.getTarget() == null) return;
        diving = true;
        net.minecraft.util.math.Vec3d dir = this.getTarget().getPos().subtract(this.getPos()).normalize();
        this.setVelocity(dir.multiply(2.0));
        this.playSound(ModSounds.SULFUR_PHANTOM_BITE, 1.0f, 0.9f);
        diveCooldown = 80;
        // Stop dive after 10 ticks
        this.getWorld().getServer().execute(() -> {
            this.setVelocity(0, 0.5, 0); // pull back up
            diving = false;
        });
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // While phased: halved damage
        if (phased) amount *= 0.5f;
        // Weak to water
        if (source.isIn(net.minecraft.registry.tag.DamageTypeTags.IS_DROWNING)) amount *= 2.0f;
        return super.damage(source, amount);
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        if (!super.tryAttack(target)) return false;
        if (target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 0));
        }
        return true;
    }

    @Override public boolean isFireImmune() { return true; }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.SULFUR_PHANTOM_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.SULFUR_PHANTOM_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.SULFUR_PHANTOM_DEATH; }
}
