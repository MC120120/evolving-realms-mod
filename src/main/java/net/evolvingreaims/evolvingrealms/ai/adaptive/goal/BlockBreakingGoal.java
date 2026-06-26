package net.evolvingreaims.evolvingrealms.ai.adaptive.goal;

import net.evolvingreaims.evolvingrealms.ai.adaptive.AdaptiveDifficultyManager;
import net.evolvingreaims.evolvingrealms.ai.adaptive.CombatProfile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

import java.util.EnumSet;

/**
 * Block Breaking Goal — active when the nearest player profile is BUILDER.
 *
 * When a player climbs a pillar (is more than 3 blocks above the mob) and the
 * game rule allows mob griefing, the mob attempts to break the lowest block
 * of the pillar so the player falls.
 */
public class BlockBreakingGoal extends Goal {

    private static final int   BREAK_INTERVAL_TICKS = 40;
    private static final float HEIGHT_THRESHOLD      = 3.0f;

    private final MobEntity mob;
    private PlayerEntity target;
    private BlockPos     targetBlock;
    private int          breakProgress = 0;
    private int          cooldown      = 0;

    public BlockBreakingGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (cooldown > 0) { cooldown--; return false; }
        if (!mob.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) return false;

        CombatProfile profile = AdaptiveDifficultyManager.INSTANCE.getNearestProfile(mob, 64.0);
        if (profile != CombatProfile.BUILDER) return false;

        PlayerEntity player = mob.getWorld().getClosestPlayer(mob, 16.0);
        if (player == null || player.isCreative()) return false;
        if (player.getY() - mob.getY() < HEIGHT_THRESHOLD) return false;

        // Find the block directly below the player that is NOT natural ground
        BlockPos below = new BlockPos((int) player.getX(), (int) (player.getY() - 1), (int) player.getZ());
        BlockState state = mob.getWorld().getBlockState(below);
        if (state.isAir() || !state.isSolid()) return false;

        this.target      = player;
        this.targetBlock = below;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (target == null || !target.isAlive()) return false;
        return target.getY() - mob.getY() >= HEIGHT_THRESHOLD && breakProgress < 100;
    }

    @Override
    public void start() {
        breakProgress = 0;
    }

    @Override
    public void tick() {
        if (targetBlock == null) return;
        mob.getLookControl().lookAt(
                targetBlock.getX() + 0.5,
                targetBlock.getY() + 0.5,
                targetBlock.getZ() + 0.5);

        breakProgress += 10;
        if (breakProgress >= 100) {
            BlockState state = mob.getWorld().getBlockState(targetBlock);
            if (!state.isAir()) {
                mob.getWorld().breakBlock(targetBlock, true, mob);
            }
            breakProgress = 0;
            cooldown      = BREAK_INTERVAL_TICKS;
        }
    }

    @Override
    public void stop() {
        target        = null;
        targetBlock   = null;
        breakProgress = 0;
    }
}
