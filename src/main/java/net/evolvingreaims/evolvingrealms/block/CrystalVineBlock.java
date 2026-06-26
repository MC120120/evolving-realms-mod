package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * Crystal Vine — glowing hanging vine that grows downward from crystal ceilings.
 * Semi-transparent and luminous.
 */
public class CrystalVineBlock extends VineBlock {

    public CrystalVineBlock(Settings settings) { super(settings); }
}
