package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Crypt Room — rows of sarcophagi (soul sand + carved stone), undead mobs,
 * and a hidden urn with rare loot.
 */
public class CryptRoom extends DungeonRoom {

    public CryptRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, width, height, depth);
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        // Two rows of sarcophagi
        for (int z = 2; z < depth - 2; z += 3) {
            BlockPos left  = origin.add(1, 1, z);
            BlockPos right = origin.add(width - 2, 1, z);

            world.setBlockState(left,        Blocks.SOUL_SAND.getDefaultState());
            world.setBlockState(left.up(),   Blocks.CARVED_PUMPKIN.getDefaultState()); // Stand-in for sarcophagus lid
            world.setBlockState(right,       Blocks.SOUL_SAND.getDefaultState());
            world.setBlockState(right.up(),  Blocks.CARVED_PUMPKIN.getDefaultState());

            // Mob spawn from sarcophagus
            if (rng.nextInt(3) == 0) {
                spawnMob(world, left.up(2),  EntityType.SKELETON);
                spawnMob(world, right.up(2), EntityType.ZOMBIE);
            }
        }

        // Central altar with hidden loot
        BlockPos altar = getCenter().add(0, 0, 0);
        world.setBlockState(altar, Blocks.STONE_BRICKS.getDefaultState());
        world.setBlockState(altar.up(), Blocks.CHEST.getDefaultState());
        if (world.getBlockEntity(altar.up()) instanceof net.minecraft.block.entity.ChestBlockEntity chest) {
            chest.setLootTable(
                    net.minecraft.util.Identifier.of("evolving_realms", "chests/dungeon_crypt"),
                    rng.nextLong());
        }

        // Eerie torches
        placeTorch(world, getCenter().add(-3, 2, 0));
        placeTorch(world, getCenter().add(3, 2, 0));
        placeTorch(world, getCenter().add(0, 2, 3));
        placeTorch(world, getCenter().add(0, 2, -3));
    }
}
