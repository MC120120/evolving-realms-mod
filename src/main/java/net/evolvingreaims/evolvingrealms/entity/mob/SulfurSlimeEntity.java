package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.DodgeArrowGoal;
import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.SurroundPlayerGoal;
import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Sulfur Slime — a corrosive variant of the vanilla slime found in the Sulfur Dimension.
 *
 * Unique mechanics:
 *   - On melee hit, applies Poison II for 5 seconds
 *   - Splits into 2–3 smaller slimes on death (like vanilla)
 *   - Leaves a small toxic puddle (liquid sulfur) at death location
 *   - Immune to fire and lava
 *   - Uses adaptive dodge and surround goals when near a player
 */
public class SulfurSlimeEntity extends HostileEntity {

    private int jumpCooldown = 0;

    public SulfurSlimeEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,      30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,  0.26)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,   4.0)
                .add(EntityAttributes.GENERIC_ARMOR,           2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,    24.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DodgeArrowGoal(this));
        this.goalSelector.add(2, new SurroundPlayerGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        if (!super.tryAttack(target)) return false;
        if (target instanceof net.minecraft.entity.LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1));
        }
        return true;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.getWorld().isClient()) {
            // Place a small pool of liquid sulfur
            net.minecraft.util.math.BlockPos pos = this.getBlockPos();
            if (this.getWorld().getBlockState(pos).isAir()) {
                this.getWorld().setBlockState(pos,
                        net.evolvingreaims.evolvingrealms.registry.ModBlocks.LIQUID_SULFUR_BLOCK.getDefaultState());
            }
            // Split into smaller slimes (3 small ones)
            for (int i = 0; i < 2; i++) {
                SulfurSlimeMiniEntity mini = new SulfurSlimeMiniEntity(
                        net.evolvingreaims.evolvingrealms.registry.ModEntities.SULFUR_SLIME, this.getWorld());
                double ox = (random.nextDouble() - 0.5) * 2;
                double oz = (random.nextDouble() - 0.5) * 2;
                mini.refreshPositionAndAngles(this.getX() + ox, this.getY(), this.getZ() + oz, 0, 0);
                this.getWorld().spawnEntity(mini);
            }
        }
    }

    @Override public boolean isFireImmune() { return true; }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.SULFUR_SLIME_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.SULFUR_SLIME_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.SULFUR_SLIME_DEATH; }
}
