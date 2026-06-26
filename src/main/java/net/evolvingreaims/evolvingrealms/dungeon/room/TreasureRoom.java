package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Treasure Room — stacked with chests, gold blocks, and guarded by a tripwire trap.
 */
public class TreasureRoom extends DungeonRoom {

    public TreasureRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, width, height, depth);
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        BlockPos centre = getCenter();

        // Central display: gold block pedestal
        world.setBlockState(centre, Blocks.GOLD_BLOCK.getDefaultState());
        world.setBlockState(centre.up(), Blocks.CHEST.getDefaultState());
        if (world.getBlockEntity(centre.up()) instanceof net.minecraft.block.entity.ChestBlockEntity chest) {
            chest.setLootTable(
                    Identifier.of("evolving_realms", "chests/dungeon_treasure"),
                    rng.nextLong());
        }

        // Corner chests
        Identifier commonLoot = Identifier.of("evolving_realms", "chests/dungeon_common");
        placeChest(world, origin.add(1, 1, 1), commonLoot);
        placeChest(world, origin.add(width - 2, 1, 1), commonLoot);
        placeChest(world, origin.add(1, 1, depth - 2), commonLoot);
        placeChest(world, origin.add(width - 2, 1, depth - 2), commonLoot);

        // Tripwire trap across doorway
        BlockPos hookA = getEntrance().add(-2, 1, 0);
        BlockPos hookB = getEntrance().add(2, 1, 0);
        world.setBlockState(hookA, Blocks.TRIPWIRE_HOOK.getDefaultState());
        world.setBlockState(hookB, Blocks.TRIPWIRE_HOOK.getDefaultState());
        world.setBlockState(hookA.add(1, 0, 0), Blocks.TRIPWIRE.getDefaultState());
        world.setBlockState(hookA.add(2, 0, 0), Blocks.TRIPWIRE.getDefaultState());
        // Connect to dispenser with arrows
        BlockPos dispenser = getEntrance().add(0, 3, 1);
        world.setBlockState(dispenser, Blocks.DISPENSER.getDefaultState());

        // Wall torches for ambiance
        placeTorch(world, centre.add(-2, 2, 0));
        placeTorch(world, centre.add(2, 2, 0));
    }
}
