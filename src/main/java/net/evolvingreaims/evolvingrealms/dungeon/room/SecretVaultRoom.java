package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Secret Vault Room — concealed behind a painting-disguised door.
 * Contains legendary loot but is guarded by a powerful trap.
 */
public class SecretVaultRoom extends DungeonRoom {

    public SecretVaultRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, Math.max(width, 7), height, Math.max(depth, 7));
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        BlockPos centre = getCenter();

        // Lava moat around central chest pedestal
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (Math.abs(dx) == 2 || Math.abs(dz) == 2) {
                    world.setBlockState(centre.add(dx, 0, dz), Blocks.LAVA.getDefaultState());
                }
            }
        }

        // Pedestal
        world.setBlockState(centre.up(0), Blocks.OBSIDIAN.getDefaultState());
        world.setBlockState(centre.up(1), Blocks.OBSIDIAN.getDefaultState());
        placeChest(world, centre.up(2),
                Identifier.of("evolving_realms", "chests/dungeon_legendary"));

        // Corner dispensers aimed inward (arrow traps)
        world.setBlockState(origin.add(0, 2, 0),             Blocks.DISPENSER.getDefaultState());
        world.setBlockState(origin.add(width - 1, 2, 0),     Blocks.DISPENSER.getDefaultState());
        world.setBlockState(origin.add(0, 2, depth - 1),     Blocks.DISPENSER.getDefaultState());
        world.setBlockState(origin.add(width - 1, 2, depth - 1), Blocks.DISPENSER.getDefaultState());

        // Soul fire torches for menace
        placeTorch(world, centre.add(-1, 3, 0));
        placeTorch(world, centre.add(1, 3, 0));
    }
}
