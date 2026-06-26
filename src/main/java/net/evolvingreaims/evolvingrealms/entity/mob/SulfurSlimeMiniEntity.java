package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/** Smaller version spawned on SulfurSlime death. No splitting on this one's death. */
public class SulfurSlimeMiniEntity extends HostileEntity {

    public SulfurSlimeMiniEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,     8.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,  2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,   16.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        if (!super.tryAttack(target)) return false;
        if (target instanceof net.minecraft.entity.LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60, 0));
        }
        return true;
    }

    @Override public boolean isFireImmune() { return true; }
    @Override protected SoundEvent getAmbientSound()              { return ModSounds.SULFUR_SLIME_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.SULFUR_SLIME_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.SULFUR_SLIME_SQUISH; }
}
