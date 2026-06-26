package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Library Room — rows of bookshelves, lecterns with random enchantments,
 * and a secret bookshelf door.
 */
public class LibraryRoom extends DungeonRoom {

    public LibraryRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, width, height, depth);
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        // Bookshelves in rows
        for (int x = 1; x < width - 1; x += 2) {
            for (int dy = 1; dy <= Math.min(height - 2, 3); dy++) {
                world.setBlockState(origin.add(x, dy, 2),         Blocks.BOOKSHELF.getDefaultState());
                world.setBlockState(origin.add(x, dy, depth - 3), Blocks.BOOKSHELF.getDefaultState());
            }
        }

        // Central reading table (crafting table + lecterns)
        BlockPos centre = getCenter();
        world.setBlockState(centre, Blocks.CRAFTING_TABLE.getDefaultState());
        world.setBlockState(centre.add(-1, 0, 0), Blocks.LECTERN.getDefaultState());
        world.setBlockState(centre.add(1, 0, 0),  Blocks.LECTERN.getDefaultState());

        // Enchanting table
        world.setBlockState(centre.add(0, 0, -2), Blocks.ENCHANTING_TABLE.getDefaultState());
        // Surround with bookshelves for max enchantment
        for (int dx = -2; dx <= 2; dx++) {
            world.setBlockState(centre.add(dx, 0, -4), Blocks.BOOKSHELF.getDefaultState());
            world.setBlockState(centre.add(dx, 0, 0),  Blocks.BOOKSHELF.getDefaultState());
        }

        // Atmospheric torches
        placeTorch(world, centre.add(-3, 3, 0));
        placeTorch(world, centre.add(3, 3, 0));
        placeTorch(world, centre.add(0, 3, 3));

        // Loot chest with rare book loot
        placeChest(world, centre.add(0, 0, 3),
                net.minecraft.util.Identifier.of("evolving_realms", "chests/dungeon_library"));
    }
}
