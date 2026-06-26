package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.DodgeArrowGoal;
import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Toxic Skeleton — a skeleton that fires tipped arrows of Poison and Slowness.
 *
 * Unique mechanics:
 *   - All fired arrows apply Poison II for 6 seconds
 *   - Every 4th arrow applies Slowness III for 3 seconds
 *   - Dodges incoming player arrows (adaptive)
 *   - Has slightly longer range than vanilla skeleton
 *   - Resistant to sunlight (does not burn)
 *   - On death: throws 2 lingering Poison II potions
 */
public class ToxicSkeletonEntity extends SkeletonEntity {

    private int arrowCount = 0;

    public ToxicSkeletonEntity(EntityType<? extends SkeletonEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return SkeletonEntity.createAbstractSkeletonAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,     22.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.26)
                .add(EntityAttributes.GENERIC_ARMOR,          2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,   40.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DodgeArrowGoal(this));
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0, 20, 15.0f));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void shootAt(net.minecraft.entity.LivingEntity target, float pullProgress) {
        arrowCount++;
        ItemStack arrowStack = new ItemStack(Items.TIPPED_ARROW);
        PersistentProjectileEntity arrow = this.createArrowProjectile(arrowStack, pullProgress, null);

        // Apply effect to arrow (Poison II always)
        if (arrow instanceof net.minecraft.entity.projectile.ArrowEntity tipped) {
            tipped.addEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    net.minecraft.entity.effect.StatusEffects.POISON, 120, 1));
            // Every 4th arrow: Slowness III
            if (arrowCount % 4 == 0) {
                tipped.addEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                        net.minecraft.entity.effect.StatusEffects.SLOWNESS, 60, 2));
            }
        }

        double dx = target.getX() - this.getX();
        double dy = target.getBodyY(0.3333) - arrow.getY();
        double dz = target.getZ() - this.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        arrow.setVelocity(dx, dy + dist * 0.2, dz, 1.6f, 14 - this.getWorld().getDifficulty().getId() * 4);

        this.playSound(ModSounds.TOXIC_SKELETON_SHOOT, 1.0f, 1.0f / (random.nextFloat() * 0.4f + 0.8f));
        this.getWorld().spawnEntity(arrow);
    }

    @Override
    protected boolean isAffectedByDaylight() { return false; } // Does not burn in sunlight

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.getWorld().isClient()) {
            // Throw 2 lingering poison potions
            for (int i = 0; i < 2; i++) {
                net.minecraft.entity.projectile.thrown.PotionEntity potion =
                        new net.minecraft.entity.projectile.thrown.PotionEntity(this.getWorld(), this);
                ItemStack potionStack = new ItemStack(Items.LINGERING_POTION);
                net.minecraft.potion.PotionContentsComponent contents =
                        new net.minecraft.potion.PotionContentsComponent(
                                java.util.Optional.empty(),
                                java.util.Optional.empty(),
                                java.util.List.of(new net.minecraft.entity.effect.StatusEffectInstance(
                                        net.minecraft.entity.effect.StatusEffects.POISON, 60, 1)));
                potionStack.set(net.minecraft.component.DataComponentTypes.POTION_CONTENTS, contents);
                potion.setStack(potionStack);
                double ox = (random.nextDouble() - 0.5) * 3;
                double oz = (random.nextDouble() - 0.5) * 3;
                potion.setVelocity(ox, 0.5 + random.nextDouble(), oz);
                potion.setPosition(this.getX(), this.getY() + 1, this.getZ());
                this.getWorld().spawnEntity(potion);
            }
        }
    }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.TOXIC_SKELETON_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.TOXIC_SKELETON_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.TOXIC_SKELETON_DEATH; }
}
