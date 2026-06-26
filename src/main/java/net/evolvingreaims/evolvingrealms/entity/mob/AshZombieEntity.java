package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.BlockBreakingGoal;
import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.SurroundPlayerGoal;
import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Ash Zombie — a zombie smothered in toxic volcanic ash.
 *
 * Unique mechanics:
 *   - Emits ash clouds on every 3rd hit, applying Blindness + Slowness
 *   - On death: creates an ash cloud (particles + AoE Wither I for 4 s)
 *   - Regenerates 2 HP every 10 seconds if standing on Ash Block
 *   - Uses Surround and BlockBreaking adaptive goals
 *   - Cannot be burned by fire or lava
 */
public class AshZombieEntity extends ZombieEntity {

    private int hitCount           = 0;
    private int ashRegenTimer      = 0;

    public AshZombieEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return ZombieEntity.createZombieAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,     35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.24)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,  6.0)
                .add(EntityAttributes.GENERIC_ARMOR,          3.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,   32.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new BlockBreakingGoal(this));
        this.goalSelector.add(2, new SurroundPlayerGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        // Ash regen on ash block
        if (!this.getWorld().isClient()) {
            ashRegenTimer++;
            if (ashRegenTimer >= 200) {
                ashRegenTimer = 0;
                net.minecraft.block.BlockState below = this.getWorld().getBlockState(this.getBlockPos().down());
                if (below.isOf(net.evolvingreaims.evolvingrealms.registry.ModBlocks.ASH_BLOCK)) {
                    this.heal(2.0f);
                }
            }
        } else {
            // Ash particle trail
            if (random.nextInt(5) == 0) {
                this.getWorld().addParticle(ParticleTypes.ASH,
                        this.getX() + (random.nextDouble() - 0.5),
                        this.getY() + random.nextDouble(),
                        this.getZ() + (random.nextDouble() - 0.5),
                        0, 0.02, 0);
            }
        }
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        if (!super.tryAttack(target)) return false;
        hitCount++;
        if (hitCount % 3 == 0 && target instanceof LivingEntity living) {
            // Ash cloud AoE
            this.getWorld().getEntitiesByClass(PlayerEntity.class,
                    this.getBoundingBox().expand(4.0),
                    net.minecraft.entity.EntityPredicates.VALID_ENTITY)
                .forEach(p -> {
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
                });
            if (!this.getWorld().isClient()) {
                for (int i = 0; i < 30; i++) {
                    double ox = (random.nextDouble() - 0.5) * 6;
                    double oz = (random.nextDouble() - 0.5) * 6;
                    this.getWorld().addParticle(ParticleTypes.ASH,
                            this.getX() + ox, this.getY() + 1, this.getZ() + oz, 0, 0.1, 0);
                }
            }
        }
        return true;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.getWorld().isClient()) {
            // Death ash cloud: Wither I
            this.getWorld().getEntitiesByClass(PlayerEntity.class,
                    this.getBoundingBox().expand(6.0),
                    net.minecraft.entity.EntityPredicates.VALID_ENTITY)
                .forEach(p -> p.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 0)));
        }
    }

    @Override public boolean isFireImmune() { return true; }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.ASH_ZOMBIE_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.ASH_ZOMBIE_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.ASH_ZOMBIE_DEATH; }
}
