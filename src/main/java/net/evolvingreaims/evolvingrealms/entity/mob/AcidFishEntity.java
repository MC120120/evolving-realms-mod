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
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Acid Fish — an aggressive piranha-like fish living in acid pools.
 *
 * Unique mechanics:
 *   - School behaviour: all Acid Fish in 10-block range converge on same target
 *   - Bite attack deals Poison I for 4 seconds
 *   - Very fast in water; sluggish on land
 *   - Emits acid drip particles while swimming
 *   - Damages players who stand in adjacent acid blocks each second
 */
public class AcidFishEntity extends WaterCreatureEntity {

    public AcidFishEntity(EntityType<? extends WaterCreatureEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return WaterCreatureEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,      8.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,  0.45)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,   3.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,    16.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.5, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        if (!super.tryAttack(target)) return false;
        if (target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 80, 0));
        }
        // Schooling: alert nearby fish
        this.getWorld().getEntitiesByClass(AcidFishEntity.class,
                this.getBoundingBox().expand(10.0),
                f -> f != this && f.getTarget() == null)
            .forEach(f -> f.setTarget((LivingEntity) target));
        return true;
    }

    @Override protected SoundEvent getAmbientSound()              { return null; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.ACID_SPIDER_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.ACID_SPIDER_DEATH; }

    @Override public boolean isPushedByFluids()                   { return false; }
    @Override public boolean canBreatheInWater()                  { return true; }
}
