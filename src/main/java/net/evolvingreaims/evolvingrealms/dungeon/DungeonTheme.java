package net.evolvingreaims.evolvingrealms.dungeon;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * Defines the visual palette and loot tier for a dungeon theme.
 *
 * Themes:
 *   CRYSTAL_CAVE       — amethyst, calcite, crystal bricks
 *   LOST_KINGDOM       — stone bricks, mossy stone, cracked stone
 *   VOLCANIC_FORTRESS  — blackstone, basalt, magma blocks
 *   UNDERGROUND_CITY   — smooth stone, chiselled stone, iron bars
 *   FROZEN_TOMB        — packed ice, blue ice, snow blocks
 *   ANCIENT_TEMPLE     — sandstone, cut sandstone, orange terracotta
 *   FORGOTTEN_MINE     — cobblestone, oak planks, rail tracks
 *   ABANDONED_LAB      — smooth stone, iron blocks, glass panes
 */
public enum DungeonTheme {

    CRYSTAL_CAVE {
        @Override public BlockState wallBlock()    { return Blocks.CALCITE.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.AMETHYST_BLOCK.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.TUFF.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.AMETHYST_CLUSTER.getDefaultState(); }
        @Override public int        lootTier()     { return 2; }
    },

    LOST_KINGDOM {
        @Override public BlockState wallBlock()    { return Blocks.STONE_BRICKS.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.MOSSY_STONE_BRICKS.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.CRACKED_STONE_BRICKS.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.STONE_BRICK_STAIRS.getDefaultState(); }
        @Override public int        lootTier()     { return 1; }
    },

    VOLCANIC_FORTRESS {
        @Override public BlockState wallBlock()    { return Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.BASALT.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.BLACKSTONE.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.MAGMA_BLOCK.getDefaultState(); }
        @Override public int        lootTier()     { return 3; }
    },

    UNDERGROUND_CITY {
        @Override public BlockState wallBlock()    { return Blocks.SMOOTH_STONE.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.CHISELED_STONE_BRICKS.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.STONE.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.IRON_BARS.getDefaultState(); }
        @Override public int        lootTier()     { return 2; }
    },

    FROZEN_TOMB {
        @Override public BlockState wallBlock()    { return Blocks.PACKED_ICE.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.BLUE_ICE.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.SNOW_BLOCK.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.ICE.getDefaultState(); }
        @Override public int        lootTier()     { return 2; }
    },

    ANCIENT_TEMPLE {
        @Override public BlockState wallBlock()    { return Blocks.SANDSTONE.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.CUT_SANDSTONE.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.SMOOTH_SANDSTONE.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.CHISELED_SANDSTONE.getDefaultState(); }
        @Override public int        lootTier()     { return 1; }
    },

    FORGOTTEN_MINE {
        @Override public BlockState wallBlock()    { return Blocks.COBBLESTONE.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.GRAVEL.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.OAK_PLANKS.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.OAK_LOG.getDefaultState(); }
        @Override public int        lootTier()     { return 1; }
    },

    ABANDONED_LAB {
        @Override public BlockState wallBlock()    { return Blocks.SMOOTH_STONE.getDefaultState(); }
        @Override public BlockState floorBlock()   { return Blocks.IRON_BLOCK.getDefaultState(); }
        @Override public BlockState ceilingBlock() { return Blocks.GLASS.getDefaultState(); }
        @Override public BlockState pillarBlock()  { return Blocks.GLASS_PANE.getDefaultState(); }
        @Override public int        lootTier()     { return 3; }
    };

    public abstract BlockState wallBlock();
    public abstract BlockState floorBlock();
    public abstract BlockState ceilingBlock();
    public abstract BlockState pillarBlock();

    /** Loot tier: 1=common, 2=rare, 3=legendary. */
    public abstract int lootTier();
}
