package net.evolvingreaims.evolvingrealms.ai.adaptive.goal;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.evolvingreaims.evolvingrealms.ai.adaptive.CombatProfile;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

/**
 * Surround Player Goal — active when the nearest player profile is MELEE_DOMINANT.
 *
 * Coordinates with other nearby mobs of the same type to spread around
 * the player from different directions rather than all rushing from one side.
 */
public class SurroundPlayerGoal extends Goal {

    private static final double SURROUND_RADIUS = 24.0;
    private static final double MOVE_SPEED       = 1.1;

    private final MobEntity mob;
    private PlayerEntity target;
    private Vec3d destinationOffset;
    private int recalcTimer = 0;

    public SurroundPlayerGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        CombatProfile profile = AdaptiveDifficultyManager.INSTANCE.getNearestProfile(mob, 64.0);
        if (profile != CombatProfile.MELEE_DOMINANT) return false;
        PlayerEntity player = mob.getWorld().getClosestPlayer(mob, SURROUND_RADIUS);
        if (player == null || player.isCreative()) return false;
        this.target = player;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return target != null && target.isAlive() && mob.squaredDistanceTo(target) < (SURROUND_RADIUS * SURROUND_RADIUS);
    }

    @Override
    public void start() {
        recalculateDestination();
    }

    @Override
    public void tick() {
        if (target == null) return;
        recalcTimer--;
        if (recalcTimer <= 0) {
            recalculateDestination();
        }
        if (destinationOffset != null) {
            Vec3d dest = target.getPos().add(destinationOffset);
            mob.getNavigation().startMovingTo(dest.x, dest.y, dest.z, MOVE_SPEED);
        }
    }

    @Override
    public void stop() {
        target = null;
        destinationOffset = null;
        mob.getNavigation().stop();
    }

    /**
     * Finds the angle around the player that is least occupied by other mobs
     * and sets the destination offset so this mob fills that gap.
     */
    private void recalculateDestination() {
        recalcTimer = 40 + mob.getRandom().nextInt(20);
        if (target == null) return;

        List<MobEntity> nearby = mob.getWorld().getEntitiesByClass(
                MobEntity.class,
                target.getBoundingBox().expand(8.0),
                e -> e != mob && e.getClass() == mob.getClass());

        // Find the angle sector (of 8 evenly spaced ones) with the fewest mobs
        int[] sectorCount = new int[8];
        for (MobEntity m : nearby) {
            Vec3d offset = m.getPos().subtract(target.getPos());
            double angle = Math.atan2(offset.z, offset.x);
            int sector   = (int) ((angle + Math.PI) / (Math.PI * 2) * 8) % 8;
            sectorCount[sector]++;
        }

        int bestSector = 0;
        for (int i = 1; i < 8; i++) {
            if (sectorCount[i] < sectorCount[bestSector]) bestSector = i;
        }

        double targetAngle = (bestSector / 8.0) * Math.PI * 2 - Math.PI;
        double dist = 3.0 + mob.getRandom().nextDouble() * 2.0;
        destinationOffset = new Vec3d(
                Math.cos(targetAngle) * dist,
                0,
                Math.sin(targetAngle) * dist);
    }
}
