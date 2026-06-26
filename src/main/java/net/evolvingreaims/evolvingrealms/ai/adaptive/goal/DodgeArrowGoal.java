package net.evolvingreaims.evolvingrealms.ai.adaptive.goal;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.evolvingreaims.evolvingrealms.ai.adaptive.CombatProfile;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

/**
 * Dodge Arrow Goal — active when the nearest player profile is BOW_DOMINANT.
 *
 * Every tick the mob checks for incoming arrows within a radius. When an
 * arrow is detected moving toward the mob, it strafes perpendicular to the
 * arrow's velocity, attempting to dodge it.
 */
public class DodgeArrowGoal extends Goal {

    private static final double DETECTION_RADIUS = 12.0;
    private static final double DODGE_SPEED       = 1.4;

    private final MobEntity mob;
    private int dodgeCooldown = 0;

    public DodgeArrowGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (dodgeCooldown > 0) { dodgeCooldown--; return false; }
        CombatProfile profile = AdaptiveDifficultyManager.INSTANCE.getNearestProfile(mob, 64.0);
        return profile == CombatProfile.BOW_DOMINANT && findIncomingArrow() != null;
    }

    @Override
    public boolean shouldContinue() {
        return dodgeCooldown > 0;
    }

    @Override
    public void start() {
        ArrowEntity arrow = findIncomingArrow();
        if (arrow == null) return;

        // Strafe perpendicular to the incoming arrow
        Vec3d arrowVel = arrow.getVelocity().normalize();
        Vec3d perpendicular = new Vec3d(-arrowVel.z, 0, arrowVel.x);

        // Randomly choose left or right
        if (mob.getRandom().nextBoolean()) perpendicular = perpendicular.negate();

        Vec3d target = mob.getPos().add(perpendicular.multiply(4.0));
        mob.getNavigation().startMovingTo(target.x, target.y, target.z, DODGE_SPEED);
        dodgeCooldown = 20 + mob.getRandom().nextInt(20);
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    private ArrowEntity findIncomingArrow() {
        Box box = mob.getBoundingBox().expand(DETECTION_RADIUS);
        List<ArrowEntity> arrows = mob.getWorld().getEntitiesByClass(ArrowEntity.class, box,
                arrow -> isArrowHeadingToward(arrow, mob));
        return arrows.isEmpty() ? null : arrows.get(0);
    }

    private boolean isArrowHeadingToward(ArrowEntity arrow, MobEntity target) {
        Vec3d toTarget   = target.getPos().subtract(arrow.getPos()).normalize();
        Vec3d arrowVel   = arrow.getVelocity().normalize();
        double dot = toTarget.dotProduct(arrowVel);
        return dot > 0.6; // roughly aimed at the mob
    }
}
