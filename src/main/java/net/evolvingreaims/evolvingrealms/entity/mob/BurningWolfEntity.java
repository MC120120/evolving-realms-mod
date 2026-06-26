package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Burning Wolf — a wolf engulfed in supernatural flame.
 *
 * Pack behaviour: spawns in groups of 2–5, coordinates attacks.
 *
 * Unique mechanics:
 *   - Always on fire; immune to fire damage
 *   - On hit: sets target on fire for 5 seconds
 *   - Howl ability (30 s cooldown): buffs all nearby Burning Wolves with Speed II for 15 s
 *   - Pack hunters: all wolves converge on a single target at once
 *   - Drops burning wolf fur (rare) and coal (common)
 */
public class BurningWolfEntity extends HostileEntity {

    private int howlCooldown = 0;

    public BurningWolfEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
        this.setOnFireFor(Integer.MAX_VALUE / 20);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,     30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.36)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,  7.0)
                .add(EntityAttributes.GENERIC_ARMOR,          2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,   32.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.4, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        // Pack cooperation: if one wolf targets a player, others follow
        this.targetSelector.add(2, new TeamRevengeGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        // Keep on fire perpetually
        if (!this.getWorld().isClient()) {
            this.setOnFireFor(100);
            if (howlCooldown > 0) howlCooldown--;
            if (howlCooldown == 0 && this.getTarget() != null) {
                howl();
                howlCooldown = 600;
            }
        } else {
            // Ember particles
            this.getWorld().addParticle(ParticleTypes.FLAME,
                    this.getX() + (random.nextDouble() - 0.5) * 0.6,
                    this.getY() + random.nextDouble() * 0.85,
                    this.getZ() + (random.nextDouble() - 0.5) * 0.6,
                    0, 0.05, 0);
        }
    }

    private void howl() {
        this.playSound(ModSounds.BURNING_WOLF_GROWL, 2.0f, 0.8f);
        // Buff all nearby Burning Wolves
        this.getWorld().getEntitiesByClass(BurningWolfEntity.class,
                this.getBoundingBox().expand(16.0),
                w -> w != this)
            .forEach(w -> w.addStatusEffect(
                    new net.minecraft.entity.effect.StatusEffectInstance(
                            net.minecraft.entity.effect.StatusEffects.SPEED, 300, 1)));
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        if (!super.tryAttack(target)) return false;
        target.setOnFireFor(5);
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isIn(net.minecraft.registry.tag.DamageTypeTags.IS_FIRE)) return false;
        return super.damage(source, amount);
    }

    @Override public boolean isFireImmune() { return true; }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.BURNING_WOLF_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.BURNING_WOLF_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.BURNING_WOLF_DEATH; }
}
