package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.evolvingreaims.evolvingrealms.registry.ModEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Combat Room — hosts a group of hostile mobs and scattered cover.
 */
public class CombatRoom extends DungeonRoom {

    public CombatRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, width, height, depth);
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        // Scatter stone/plank cover blocks
        int coverCount = 3 + rng.nextInt(4);
        for (int i = 0; i < coverCount; i++) {
            int x = 2 + rng.nextInt(width - 4);
            int z = 2 + rng.nextInt(depth - 4);
            world.setBlockState(origin.add(x, 1, z), theme.wallBlock());
            if (rng.nextBoolean()) {
                world.setBlockState(origin.add(x, 2, z), theme.wallBlock());
            }
        }

        // Spawn 2–5 mobs
        int mobCount = 2 + rng.nextInt(4);
        EntityType<?>[] pool = {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            ModEntities.TOXIC_SKELETON,
            ModEntities.ASH_ZOMBIE,
            ModEntities.CRYSTAL_BEETLE
        };
        for (int i = 0; i < mobCount; i++) {
            int x = 2 + rng.nextInt(width - 4);
            int z = 2 + rng.nextInt(depth - 4);
            EntityType<?> type = pool[rng.nextInt(pool.length)];
            spawnMob(world, origin.add(x, 1, z), type);
        }

        // Wall-mounted torches
        placeTorch(world, getCenter().add(-1, 2, 0));
        placeTorch(world, getCenter().add(1, 2, 0));
    }
}
