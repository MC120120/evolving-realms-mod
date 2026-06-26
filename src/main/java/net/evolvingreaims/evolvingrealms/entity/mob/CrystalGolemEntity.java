package net.evolvingreaims.evolvingrealms.entity.mob;

import net.evolvingreaims.evolvingrealms.ai.adaptive.goal.BlockBreakingGoal;
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
 * Crystal Golem — a massive crystalline construct, the elite enemy of the Sulfur Dimension.
 *
 * Unique mechanics:
 *   - Very high health and armor; slow but hits like a truck
 *   - Crystal Shatter passive: 25 % chance to reflect 40 % melee damage back
 *   - Ground Pound ability every 10 s: knocks back and blinds all players in 5-block radius
 *   - Crystal Shard drop: deals 8 piercing damage per hit
 *   - BlockBreaking goal: destroys player shelters
 *   - Immune to projectiles (arrows bounce off, dealing 0 damage)
 */
public class CrystalGolemEntity extends HostileEntity {

    private int groundPoundCooldown = 0;

    public CrystalGolemEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,           150.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,       0.16)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,        20.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8)
                .add(EntityAttributes.GENERIC_ARMOR,                14.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,       8.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,         24.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,      3.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new BlockBreakingGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 0.8, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.6));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient()) {
            if (groundPoundCooldown > 0) groundPoundCooldown--;
            if (groundPoundCooldown == 0 && this.getTarget() != null
                    && this.squaredDistanceTo(this.getTarget()) < 64.0) {
                groundPound();
                groundPoundCooldown = 200;
            }
        } else {
            // Sparkle particles
            if (random.nextInt(4) == 0) {
                this.getWorld().addParticle(ParticleTypes.END_ROD,
                        this.getX() + (random.nextDouble() - 0.5) * 1.4,
                        this.getY() + random.nextDouble() * 2.9,
                        this.getZ() + (random.nextDouble() - 0.5) * 1.4,
                        0, 0.05, 0);
            }
        }
    }

    private void groundPound() {
        this.playSound(ModSounds.CRYSTAL_GOLEM_SHATTER, 1.5f, 0.8f);
        this.getWorld().getEntitiesByClass(PlayerEntity.class,
                this.getBoundingBox().expand(5.0),
                net.minecraft.entity.EntityPredicates.VALID_ENTITY)
            .forEach(p -> {
                p.damage(this.getDamageSources().generic(), 12.0f);
                p.addVelocity(0, 0.8, 0);
                p.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                        net.minecraft.entity.effect.StatusEffects.BLINDNESS, 60, 0));
            });
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // Projectile immunity
        if (source.isIn(net.minecraft.registry.tag.DamageTypeTags.IS_PROJECTILE)) return false;
        // Crystal shatter: 25 % reflect chance
        if (source.getAttacker() instanceof LivingEntity attacker && random.nextInt(4) == 0) {
            attacker.damage(this.getDamageSources().generic(), amount * 0.4f);
        }
        return super.damage(source, amount);
    }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.CRYSTAL_GOLEM_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.CRYSTAL_GOLEM_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.CRYSTAL_GOLEM_DEATH; }
    @Override protected SoundEvent getStepSound()                 { return ModSounds.CRYSTAL_GOLEM_STEP; }
}
