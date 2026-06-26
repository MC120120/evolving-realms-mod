package net.evolvingreaims.evolvingrealms.entity.boss;

import net.evolvingreaims.evolvingrealms.registry.ModEntities;
import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Ancient Sulfur Titan — the multi-phase boss of the Sulfur Dimension.
 *
 * Phase thresholds:
 *   Phase 1: 100 % → 66 % HP  — standard attacks
 *   Phase 2:  66 % → 33 % HP  — rage, increased speed, new abilities
 *   Phase 3:  33 % →  0 % HP  — minion summons, enrage mode, fire breath
 */
public class AncientSulfurTitanEntity extends HostileEntity {

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------
    private final ServerBossBar bossBar = new ServerBossBar(
            Text.translatable("entity.evolving_realms.ancient_sulfur_titan"),
            BossBar.Color.YELLOW,
            BossBar.Style.NOTCHED_10);

    public enum Phase { ONE, TWO, THREE }

    private Phase currentPhase = Phase.ONE;
    private int attackCooldown = 0;
    private int abilityTimer   = 0;
    private boolean hasEnteredPhase2 = false;
    private boolean hasEnteredPhase3 = false;

    // -------------------------------------------------------------------------
    // Constructor & Attributes
    // -------------------------------------------------------------------------
    public AncientSulfurTitanEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
        this.experiencePoints = 500;
        this.setHealth(this.getMaxHealth());
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,       1200.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,   0.22)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,    28.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_ARMOR,            20.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,  10.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,     64.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 3.0);
    }

    // -------------------------------------------------------------------------
    // Goal Initialisation
    // -------------------------------------------------------------------------
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 24.0f));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    // -------------------------------------------------------------------------
    // Tick
    // -------------------------------------------------------------------------
    @Override
    public void tick() {
        super.tick();
        updateBossBar();
        checkPhaseTransitions();

        if (!this.getWorld().isClient()) {
            if (attackCooldown > 0) attackCooldown--;
            abilityTimer++;

            if (abilityTimer % 80 == 0) {
                useAbility();
            }

            spawnAmbientParticles();
        }
    }

    // -------------------------------------------------------------------------
    // Phase Transitions
    // -------------------------------------------------------------------------
    private void checkPhaseTransitions() {
        float healthPercent = this.getHealth() / this.getMaxHealth();

        if (!hasEnteredPhase2 && healthPercent <= 0.66f) {
            hasEnteredPhase2 = true;
            enterPhase(Phase.TWO);
        } else if (!hasEnteredPhase3 && healthPercent <= 0.33f) {
            hasEnteredPhase3 = true;
            enterPhase(Phase.THREE);
        }
    }

    private void enterPhase(Phase phase) {
        this.currentPhase = phase;
        this.playSound(ModSounds.TITAN_PHASE_CHANGE, 2.0f, phase == Phase.TWO ? 0.9f : 0.7f);
        this.playSound(ModSounds.TITAN_ROAR, 2.0f, 1.0f);

        if (!this.getWorld().isClient()) {
            // Visual burst of particles
            for (int i = 0; i < 40; i++) {
                double ox = (random.nextDouble() - 0.5) * 6;
                double oy = random.nextDouble() * 4;
                double oz = (random.nextDouble() - 0.5) * 6;
                this.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                        this.getX() + ox, this.getY() + oy, this.getZ() + oz, 0, 0, 0);
            }
        }

        switch (phase) {
            case TWO -> {
                // Increase speed and damage in phase 2
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(0.30);
                this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                        .setBaseValue(36.0);
                this.bossBar.setColor(BossBar.Color.RED);
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1, false, false));
                broadcastTitle("boss.evolving_realms.titan.phase2");
            }
            case THREE -> {
                // Rage mode in phase 3
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(0.38);
                this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                        .setBaseValue(46.0);
                this.bossBar.setColor(BossBar.Color.PURPLE);
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 2, false, false));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,    Integer.MAX_VALUE, 1, false, false));
                this.playSound(ModSounds.TITAN_RAGE, 2.0f, 1.0f);
                broadcastTitle("boss.evolving_realms.titan.phase3");
            }
            default -> {}
        }
    }

    // -------------------------------------------------------------------------
    // Abilities
    // -------------------------------------------------------------------------
    private void useAbility() {
        if (this.getTarget() == null) return;
        int roll = this.random.nextInt(currentPhase == Phase.ONE ? 3 : currentPhase == Phase.TWO ? 4 : 5);
        switch (roll) {
            case 0 -> groundSlam();
            case 1 -> crystalSpike();
            case 2 -> lavaExplosion();
            case 3 -> poisonCloud();
            case 4 -> {
                fireBreath();
                summonMinions();
            }
        }
    }

    /** Shockwave that damages and launches nearby players. */
    private void groundSlam() {
        this.playSound(ModSounds.TITAN_GROUND_SLAM, 2.0f, 1.0f);
        Box area = this.getBoundingBox().expand(8.0);
        List<PlayerEntity> players = this.getWorld().getEntitiesByClass(PlayerEntity.class, area, EntityPredicates.VALID_ENTITY);
        for (PlayerEntity player : players) {
            player.damage(this.getDamageSources().generic(), 18.0f);
            player.addVelocity(0, 1.2, 0);
        }
        // Summon explosion-like effect (non-destructive)
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(),
                4.0f, false, World.ExplosionSourceType.NONE);
    }

    /** Summons a ring of crystal spikes around the titan. */
    private void crystalSpike() {
        this.playSound(ModSounds.TITAN_CRYSTAL_SPIKE, 2.0f, 1.0f);
        if (!this.getWorld().isClient()) {
            for (int i = 0; i < 8; i++) {
                double angle = (Math.PI * 2 / 8) * i;
                double x = this.getX() + Math.cos(angle) * 5;
                double z = this.getZ() + Math.sin(angle) * 5;
                // Spawn crystal spike projectile
                CrystalSpikeEntity spike = new CrystalSpikeEntity(this.getWorld(), this);
                spike.setPosition(x, this.getY() + 1, z);
                this.getWorld().spawnEntity(spike);
            }
        }
    }

    /** Creates a large lava explosion at the target's feet. */
    private void lavaExplosion() {
        if (this.getTarget() == null) return;
        this.playSound(ModSounds.TITAN_LAVA_EXPLOSION, 2.0f, 0.9f);
        Vec3d pos = this.getTarget().getPos();
        this.getWorld().createExplosion(this, pos.x, pos.y, pos.z,
                6.0f, true, World.ExplosionSourceType.MOB);
    }

    /** Applies wither and poison to all players in a large radius. */
    private void poisonCloud() {
        Box area = this.getBoundingBox().expand(12.0);
        List<PlayerEntity> players = this.getWorld().getEntitiesByClass(PlayerEntity.class, area, EntityPredicates.VALID_ENTITY);
        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,  80, 0));
        }
        if (!this.getWorld().isClient()) {
            for (int i = 0; i < 80; i++) {
                double ox = (random.nextDouble() - 0.5) * 24;
                double oz = (random.nextDouble() - 0.5) * 24;
                this.getWorld().addParticle(ParticleTypes.SMOKE,
                        this.getX() + ox, this.getY() + 1, this.getZ() + oz, 0, 0.05, 0);
            }
        }
    }

    /** Sprays fire in a cone towards the target. */
    private void fireBreath() {
        if (this.getTarget() == null) return;
        this.playSound(ModSounds.TITAN_FIRE_BREATH, 2.0f, 1.0f);
        Vec3d direction = this.getTarget().getPos().subtract(this.getPos()).normalize();
        if (!this.getWorld().isClient()) {
            for (int i = 0; i < 20; i++) {
                double spread = (random.nextDouble() - 0.5) * 0.4;
                FireBreathProjectile projectile = new FireBreathProjectile(this.getWorld(), this);
                projectile.setVelocity(direction.x + spread, direction.y + spread * 0.5, direction.z + spread, 1.5f, 0);
                projectile.setPosition(this.getX(), this.getBodyY(0.7), this.getZ());
                this.getWorld().spawnEntity(projectile);
            }
        }
    }

    /** Summons minions during Phase 3. */
    private void summonMinions() {
        if (currentPhase != Phase.THREE) return;
        this.playSound(ModSounds.TITAN_SUMMON_MINION, 2.0f, 1.0f);
        int count = 2 + random.nextInt(3);
        for (int i = 0; i < count; i++) {
            double ox = (random.nextDouble() - 0.5) * 10;
            double oz = (random.nextDouble() - 0.5) * 10;
            BlockPos pos = new BlockPos((int)(this.getX() + ox), (int)this.getY(), (int)(this.getZ() + oz));

            // Alternate between sulfur slimes and ash zombies as minions
            LivingEntity minion = (i % 2 == 0)
                    ? ModEntities.SULFUR_SLIME.create(this.getWorld())
                    : ModEntities.ASH_ZOMBIE.create(this.getWorld());
            if (minion != null) {
                minion.refreshPositionAndAngles(pos, 0, 0);
                this.getWorld().spawnEntity(minion);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Boss Bar
    // -------------------------------------------------------------------------
    private void updateBossBar() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    // -------------------------------------------------------------------------
    // Death
    // -------------------------------------------------------------------------
    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        this.playSound(ModSounds.TITAN_DEATH, 2.0f, 0.8f);
        if (!this.getWorld().isClient()) {
            // Spectacular death explosion cascade
            for (int i = 0; i < 10; i++) {
                double ox = (random.nextDouble() - 0.5) * 8;
                double oy = random.nextDouble() * 6;
                double oz = (random.nextDouble() - 0.5) * 8;
                this.getWorld().createExplosion(this,
                        this.getX() + ox, this.getY() + oy, this.getZ() + oz,
                        3.5f, false, World.ExplosionSourceType.NONE);
            }
            broadcastTitle("boss.evolving_realms.titan.defeated");
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private void spawnAmbientParticles() {
        if (!this.getWorld().isClient()) return;
        for (int i = 0; i < 4; i++) {
            double ox = (random.nextDouble() - 0.5) * 4;
            double oz = (random.nextDouble() - 0.5) * 4;
            this.getWorld().addParticle(ParticleTypes.LAVA,
                    this.getX() + ox, this.getY() + 0.5, this.getZ() + oz,
                    0, 0.1, 0);
        }
    }

    private void broadcastTitle(String translationKey) {
        this.bossBar.getPlayers().forEach(p ->
                p.sendMessage(Text.translatable(translationKey), true));
    }

    // -------------------------------------------------------------------------
    // NBT persistence
    // -------------------------------------------------------------------------
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Phase", currentPhase.name());
        nbt.putBoolean("EnteredPhase2", hasEnteredPhase2);
        nbt.putBoolean("EnteredPhase3", hasEnteredPhase3);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Phase")) {
            currentPhase = Phase.valueOf(nbt.getString("Phase"));
        }
        hasEnteredPhase2 = nbt.getBoolean("EnteredPhase2");
        hasEnteredPhase3 = nbt.getBoolean("EnteredPhase3");
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    // -------------------------------------------------------------------------
    // Sounds
    // -------------------------------------------------------------------------
    @Override @Nullable protected SoundEvent getAmbientSound()  { return ModSounds.TITAN_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource s) { return ModSounds.TITAN_HURT; }
    @Override protected SoundEvent getDeathSound()              { return ModSounds.TITAN_DEATH; }
    @Override protected SoundEvent getStepSound()               { return ModSounds.TITAN_STEP; }

    // -------------------------------------------------------------------------
    // Misc
    // -------------------------------------------------------------------------
    @Override public boolean isFireImmune()    { return true; }
    @Override public boolean canHaveStatusEffect(StatusEffectInstance e) { return false; }

    public Phase getCurrentPhase() { return currentPhase; }
}
