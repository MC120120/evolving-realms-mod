package net.evolvingreaims.evolvingrealms.dungeon.room;

import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Puzzle Room — pressure-plate-and-piston lock.
 *
 * The door to the next room is sealed by a locked iron door.
 * To open it, the player must step on the correct pressure plate
 * out of three; the wrong ones trigger arrow dispensers.
 */
public class PuzzleRoom extends DungeonRoom {

    public PuzzleRoom(BlockPos origin, int width, int height, int depth) {
        super(origin, width, height, depth);
    }

    @Override
    protected void placeInterior(ServerWorld world, DungeonTheme theme, Random rng) {
        BlockPos centre = getCenter();
        int correctPlate = rng.nextInt(3);

        // Three pressure plates in a row
        for (int i = 0; i < 3; i++) {
            BlockPos platePos = centre.add(i - 1, 0, 2);
            if (i == correctPlate) {
                world.setBlockState(platePos, Blocks.STONE_PRESSURE_PLATE.getDefaultState());
            } else {
                world.setBlockState(platePos, Blocks.STONE_PRESSURE_PLATE.getDefaultState());
                // Wrong plates connect to dispensers via redstone (simplified placement)
                world.setBlockState(platePos.down(), Blocks.REDSTONE_WIRE.getDefaultState());
                world.setBlockState(platePos.add(0, -1, 1), Blocks.DISPENSER.getDefaultState());
            }
        }

        // Iron door exit
        BlockPos doorBottom = centre.add(0, 0, -2);
        world.setBlockState(doorBottom, Blocks.IRON_DOOR.getDefaultState());
        world.setBlockState(doorBottom.up(), Blocks.IRON_DOOR.getDefaultState());

        // Clue sign above the door
        world.setBlockState(doorBottom.up(2),
                Blocks.OAK_WALL_SIGN.getDefaultState());

        // Atmospheric torches
        placeTorch(world, centre.add(-2, 2, 0));
        placeTorch(world, centre.add(2, 2, 0));
    }
}
