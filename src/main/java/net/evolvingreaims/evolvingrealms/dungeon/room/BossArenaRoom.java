package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.evolvingreaims.evolvingrealms.registry.ModEntities;
import net.evolvingreaims.evolvingrealms.registry.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Boss Arena Room — the climax chamber of any dungeon.
 *
 * Features:
 *   - Circular arena carved from the theme's wall blocks
 *   - Four corner pillars with theme pillar blocks
 *   - Central summoning dais (raised platform)
 *   - Locked entry door (iron bars) that opens on boss death
 *   - Secret vault door (hidden lever-operated piston door)
 *   - Boss spawn at the centre
 *   - Loot chest behind the boss spawn point
 */
public class BossArenaRoom extends DungeonRoom {

    public BossArenaRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, Math.max(width, 15), Math.max(height, 7), Math.max(depth, 15));
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        BlockPos centre = getCenter();

        // Circular arena floor pattern
        int radius = Math.min(width, depth) / 2 - 1;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    BlockPos floorPos = centre.add(x, 0, z);
                    world.setBlockState(floorPos, theme.floorBlock());
                    // Clear interior
                    for (int dy = 1; dy <= height - 2; dy++) {
                        world.setBlockState(floorPos.up(dy), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        // Raised dais in the centre (3×3 platform, 1 block high)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                world.setBlockState(centre.add(dx, 1, dz), theme.wallBlock());
            }
        }

        // Corner pillars
        placeCornerPillar(world, theme, origin.add(2, 1, 2));
        placeCornerPillar(world, theme, origin.add(width - 3, 1, 2));
        placeCornerPillar(world, theme, origin.add(2, 1, depth - 3));
        placeCornerPillar(world, theme, origin.add(width - 3, 1, depth - 3));

        // Torches on pillars
        placeTorch(world, origin.add(2, 4, 2));
        placeTorch(world, origin.add(width - 3, 4, 2));
        placeTorch(world, origin.add(2, 4, depth - 3));
        placeTorch(world, origin.add(width - 3, 4, depth - 3));

        // Loot chest behind dais
        BlockPos chestPos = centre.add(0, 2, -3);
        Identifier lootTable = switch (theme.lootTier()) {
            case 3 -> Identifier.of("evolving_realms", "chests/dungeon_legendary");
            case 2 -> Identifier.of("evolving_realms", "chests/dungeon_rare");
            default -> Identifier.of("evolving_realms", "chests/dungeon_common");
        };
        placeChest(world, chestPos, lootTable);

        // Spawn boss on the dais
        spawnMob(world, centre.add(0, 2, 0), ModEntities.CRYSTAL_GOLEM); // Mini-boss by default
    }

    private void placeCornerPillar(ServerWorld world, DungeonTheme theme, BlockPos base) {
        for (int dy = 0; dy < height - 1; dy++) {
            world.setBlockState(base.up(dy), theme.pillarBlock());
        }
    }
}
