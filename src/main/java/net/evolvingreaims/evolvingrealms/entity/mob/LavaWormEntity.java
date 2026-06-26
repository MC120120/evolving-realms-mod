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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Lava Worm — a giant burrowing worm that lives in lava pools of the Sulfur Dimension.
 *
 * Unique mechanics:
 *   - Can burrow underground (becomes invulnerable while burrowed)
 *   - Surfaces near the player and immediately hits (ambush)
 *   - Sprays molten lava balls in 5-directional burst
 *   - Immune to fire, lava, and knockback
 *   - On death: spawns 3 lava pillars at random nearby positions
 */
public class LavaWormEntity extends HostileEntity {

    public enum State { SURFACE, BURROWING, BURROWED, EMERGING }

    private State   state         = State.SURFACE;
    private int     stateTimer    = 0;
    private int     lavaSprayCooldown = 0;
    private BlockPos burrowTarget = null;

    public LavaWormEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,           120.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,       0.18)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,        14.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_ARMOR,                10.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,         32.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.6));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 16.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        stateTimer++;
        if (lavaSprayCooldown > 0) lavaSprayCooldown--;

        if (!this.getWorld().isClient()) {
            switch (state) {
                case SURFACE -> {
                    // Decide to burrow after 6–12 seconds near player
                    if (stateTimer > 120 + random.nextInt(120) && this.getTarget() != null) {
                        startBurrowing();
                    }
                    if (lavaSprayCooldown == 0 && this.getTarget() != null
                            && this.squaredDistanceTo(this.getTarget()) < 200) {
                        lavaSpray();
                        lavaSprayCooldown = 100;
                    }
                }
                case BURROWING -> {
                    if (stateTimer > 20) {
                        state      = State.BURROWED;
                        stateTimer = 0;
                        this.setInvisible(true);
                        this.setInvulnerable(true);
                        teleportNearPlayer();
                    }
                }
                case BURROWED -> {
                    if (stateTimer > 40) {
                        state      = State.EMERGING;
                        stateTimer = 0;
                    }
                }
                case EMERGING -> {
                    if (stateTimer > 20) {
                        state      = State.SURFACE;
                        stateTimer = 0;
                        this.setInvisible(false);
                        this.setInvulnerable(false);
                        this.playSound(ModSounds.LAVA_WORM_EMERGE, 1.5f, 0.9f);
                        // Immediate surface strike
                        if (this.getTarget() != null) this.tryAttack(this.getTarget());
                    }
                }
            }
        } else {
            // Surface lava trail particles
            if (state == State.SURFACE && random.nextInt(4) == 0) {
                this.getWorld().addParticle(ParticleTypes.LAVA,
                        this.getX() + (random.nextDouble() - 0.5),
                        this.getY() + 0.2,
                        this.getZ() + (random.nextDouble() - 0.5),
                        0, 0.05, 0);
            }
        }
    }

    private void startBurrowing() {
        state      = State.BURROWING;
        stateTimer = 0;
        this.playSound(ModSounds.LAVA_WORM_BURROW, 1.5f, 1.0f);
        this.getNavigation().stop();
    }

    private void teleportNearPlayer() {
        if (this.getTarget() == null) return;
        double ox = (random.nextDouble() - 0.5) * 10;
        double oz = (random.nextDouble() - 0.5) * 10;
        this.setPosition(this.getTarget().getX() + ox, this.getTarget().getY(), this.getTarget().getZ() + oz);
    }

    private void lavaSpray() {
        this.playSound(ModSounds.LAVA_WORM_SHOOT, 1.2f, 1.0f);
        // Shoot 5 lava balls in spread pattern
        for (int i = 0; i < 5; i++) {
            double angle = (Math.PI * 2 / 5) * i;
            double ox = Math.cos(angle) * 0.5;
            double oz = Math.sin(angle) * 0.5;
            net.minecraft.entity.projectile.SmallFireballEntity fireball =
                    new net.minecraft.entity.projectile.SmallFireballEntity(
                            this.getWorld(), this, ox, 0.4, oz);
            fireball.setPosition(this.getX(), this.getBodyY(0.7), this.getZ());
            this.getWorld().spawnEntity(fireball);
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.getWorld().isClient()) {
            for (int i = 0; i < 3; i++) {
                double ox = (random.nextDouble() - 0.5) * 8;
                double oz = (random.nextDouble() - 0.5) * 8;
                BlockPos lavaPos = new BlockPos(
                        (int)(this.getX() + ox), (int)this.getY(),
                        (int)(this.getZ() + oz));
                this.getWorld().setBlockState(lavaPos, net.minecraft.block.Blocks.LAVA.getDefaultState());
            }
        }
    }

    @Override public boolean isFireImmune() { return true; }

    @Override protected SoundEvent getAmbientSound()              { return ModSounds.LAVA_WORM_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return ModSounds.LAVA_WORM_HURT; }
    @Override protected SoundEvent getDeathSound()                { return ModSounds.LAVA_WORM_DEATH; }
}
