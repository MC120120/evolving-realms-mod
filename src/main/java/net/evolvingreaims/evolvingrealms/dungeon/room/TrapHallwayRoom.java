package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Trap Hallway Room — a narrow corridor lined with arrow dispensers,
 * tripwires, pressure plates, and floor pitfalls.
 */
public class TrapHallwayRoom extends DungeonRoom {

    public TrapHallwayRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, 5, height, Math.max(depth, 14));
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        // Place arrow dispensers alternating sides every 3 blocks
        for (int z = 3; z < depth - 3; z += 3) {
            boolean leftSide = (z / 3) % 2 == 0;
            int     x        = leftSide ? 0 : width - 1;
            BlockPos dispPos = origin.add(x, 2, z);
            world.setBlockState(dispPos, Blocks.DISPENSER.getDefaultState());

            // Tripwire to trigger
            BlockPos wireA = origin.add(1, 1, z);
            BlockPos wireB = origin.add(3, 1, z);
            world.setBlockState(wireA.add(-1, 0, 0), Blocks.TRIPWIRE_HOOK.getDefaultState());
            world.setBlockState(wireA, Blocks.TRIPWIRE.getDefaultState());
            world.setBlockState(wireB, Blocks.TRIPWIRE.getDefaultState());
            world.setBlockState(wireB.add(1, 0, 0), Blocks.TRIPWIRE_HOOK.getDefaultState());
        }

        // Pitfall every 5 blocks (2×1 hole with lava below)
        for (int z = 5; z < depth - 5; z += 5) {
            if (rng.nextBoolean()) {
                BlockPos pitPos = origin.add(2, 0, z);
                world.setBlockState(pitPos,         Blocks.AIR.getDefaultState());
                world.setBlockState(pitPos.add(1, 0, 0), Blocks.AIR.getDefaultState());
                world.setBlockState(pitPos.down(),  Blocks.LAVA.getDefaultState());
            }
        }
    }
}
