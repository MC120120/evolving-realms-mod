package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.DodgeArrowGoal;
import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Acid Spider — an aggressive spider variant that spits acid and web-cocoons targets.
 *
 * Unique mechanics:
 *   - Spits acid projectiles (Slowness + Weakness) every 4 seconds
 *   - On melee hit: applies Weakness II for 3 seconds
 *   - Can climb walls like vanilla spider
 *   - Emits acid drip particles near the player
 *   - Adaptive dodge vs. bow users
 */
public class AcidSpiderEntity extends SpiderEntity {

    private int acidSpitCooldown = 0;

    public AcidSpiderEntity(EntityType<? extends SpiderEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return SpiderEntity.createSpiderAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,     28.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,  3.0)
                .add(EntityAttributes.GENERIC_ARMOR,          2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,   36.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DodgeArrowGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient()) {
            if (acidSpitCooldown > 0) acidSpitCooldown--;
            if (acidSpitCooldown == 0 && this.getTarget() != null) {
                acidSpit();
                acidSpitCooldown = 80;
            }
        } else {
            // Client: emit acid drip particles
            if (random.nextInt(8) == 0) {
                this.getWorld().addParticle(ParticleTypes.DRIPPING_WATER,
                        this.getX(), this.getY() + 0.5, this.getZ(), 0, -0.1, 0);
            }
        }
    }

    private void acidSpit() {
        LivingEntity target = this.getTarget();
        if (target == null) return;
        AcidSpitProjectile projectile = new AcidSpitProjectile(this.getWorld(), this);
        double dx = target.getX() - this.getX();
        double dy = target.getBodyY(0.3333) - projectile.getY();
        double dz = target.getZ() - this.getZ();
        projectile.setVelocity(dx, dy + 0.1, dz, 1.6f, 4.0f);
        this.getWorld().spawnEntity(projectile);
        this.playSound(ModSounds.ACID_SPIDER_SPIT, 1.0f, 1.0f + random.nextFloat() * 0.2f);
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        if (!super.tryAttack(target)) return false;
        if (target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 1));
        }
        return true;
    }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.ACID_SPIDER_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.ACID_SPIDER_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.ACID_SPIDER_DEATH; }
}
