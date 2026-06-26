package net.evolvingreaims.evolvingrealms.village.task;

import net.evolvingreaims.evolvingrealms.village.VillageManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskRunnable;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

/**
 * Builder Villager Task — when a village needs a structure, the assigned
 * Builder villager walks to the build site and places blocks over time.
 * One block per task tick (approximately every second).
 */
public class BuilderVillagerTask implements TaskRunnable<VillagerEntity> {

    private static final int BUILD_RANGE = 32;
    private int blocksPlaced = 0;
    private BlockPos currentTarget;

    @Override
    public boolean trigger(ServerWorld world, VillagerEntity villager, long time) {
        // Check if there is a pending build job from VillageManager
        var data = VillageManager.getInstance().getNearestVillageData(
                world, villager.getBlockPos(), BUILD_RANGE);
        return data != null && data.hasPendingBuild();
    }

    @Override
    public void run(ServerWorld world, VillagerEntity villager, long time) {
        var data = VillageManager.getInstance().getNearestVillageData(
                world, villager.getBlockPos(), BUILD_RANGE);
        if (data == null || !data.hasPendingBuild()) return;

        BlockPos site = data.getNextBuildBlockPos();
        if (site == null) return;

        currentTarget = site;

        // Walk toward the build site
        villager.getBrain().doExclusively(
                net.minecraft.entity.ai.brain.MemoryModuleType.WALK_TARGET,
                new net.minecraft.entity.ai.brain.WalkTarget(site, 0.5f, 2));

        // If within reach, place a block
        if (villager.getBlockPos().isWithinDistance(site, 3.0)) {
            if (world.getBlockState(site).isAir()) {
                world.setBlockState(site, Blocks.OAK_PLANKS.getDefaultState());
                data.markBuildBlockPlaced(site);
                blocksPlaced++;
            }
        }
    }

    @Override
    public boolean shouldKeepRunning(ServerWorld world, VillagerEntity villager, long time) {
        var data = VillageManager.getInstance().getNearestVillageData(
                world, villager.getBlockPos(), BUILD_RANGE);
        return data != null && data.hasPendingBuild() && blocksPlaced < 200;
    }

    @Override
    public void stop(ServerWorld world, VillagerEntity villager, long time) {
        blocksPlaced = 0;
        currentTarget = null;
    }
}
