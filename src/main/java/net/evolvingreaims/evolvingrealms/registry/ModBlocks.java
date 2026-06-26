package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.block.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public final class ModBlocks {

    public static final Block SULFUR_BLOCK = register("sulfur_block",
            new SulfurBlock(Block.Settings.create()
                    .strength(2.5f, 3.0f).sounds(ModSoundGroups.SULFUR).luminance(state -> 2)));

    public static final Block SULFUR_ORE = register("sulfur_ore",
            new SulfurOreBlock(Block.Settings.create()
                    .strength(3.0f, 3.0f).sounds(BlockSoundGroup.STONE).requiresTool()));

    public static final Block DEEPSLATE_SULFUR_ORE = register("deepslate_sulfur_ore",
            new SulfurOreBlock(Block.Settings.create()
                    .strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE).requiresTool()));

    public static final Block PYRITE_ORE = register("pyrite_ore",
            new OreBlock(Block.Settings.create()
                    .strength(3.5f, 3.0f).sounds(BlockSoundGroup.STONE).requiresTool()));

    public static final Block VOLCANIUM_ORE = register("volcanium_ore",
            new VolcaniumOreBlock(Block.Settings.create()
                    .strength(4.0f, 4.5f).sounds(ModSoundGroups.VOLCANIC).requiresTool().luminance(state -> 4)));

    public static final Block RAW_SULFUR_BLOCK = register("raw_sulfur_block",
            new Block(Block.Settings.create().strength(2.0f, 2.0f).sounds(ModSoundGroups.SULFUR)));

    public static final Block PYRITE_BLOCK = register("pyrite_block",
            new Block(Block.Settings.create().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL).requiresTool()));

    public static final Block VOLCANIUM_BLOCK = register("volcanium_block",
            new VolcaniumBlock(Block.Settings.create()
                    .strength(6.0f, 8.0f).sounds(ModSoundGroups.VOLCANIC).requiresTool().luminance(state -> 6)));

    public static final Block SULFUR_CRYSTAL = register("sulfur_crystal",
            new CrystalBlock(Block.Settings.create()
                    .strength(1.5f, 2.0f).sounds(ModSoundGroups.CRYSTAL).luminance(state -> 10).nonOpaque()));

    public static final Block CRYSTAL_GLASS = register("crystal_glass",
            new CrystalGlassBlock(Block.Settings.create()
                    .strength(0.3f).sounds(BlockSoundGroup.GLASS).luminance(state -> 7).nonOpaque()
                    .allowsSpawning((s, w, p, t) -> false)));

    public static final Block CRYSTAL_BRICKS = register("crystal_bricks",
            new Block(Block.Settings.create()
                    .strength(3.0f, 4.0f).sounds(ModSoundGroups.CRYSTAL).luminance(state -> 5).requiresTool()));

    public static final Block CRYSTAL_BRICKS_SLAB = register("crystal_bricks_slab",
            new SlabBlock(Block.Settings.copy(CRYSTAL_BRICKS)));

    public static final Block CRYSTAL_BRICKS_STAIRS = register("crystal_bricks_stairs",
            new StairsBlock(CRYSTAL_BRICKS.getDefaultState(), Block.Settings.copy(CRYSTAL_BRICKS)));

    public static final Block CRYSTAL_BRICKS_WALL = register("crystal_bricks_wall",
            new WallBlock(Block.Settings.copy(CRYSTAL_BRICKS)));

    public static final Block ACID_STONE = register("acid_stone",
            new AcidStoneBlock(Block.Settings.create()
                    .strength(2.0f, 3.0f).sounds(BlockSoundGroup.STONE).requiresTool()));

    public static final Block ACID_STONE_SLAB = register("acid_stone_slab",
            new SlabBlock(Block.Settings.copy(ACID_STONE)));

    public static final Block ACID_STONE_STAIRS = register("acid_stone_stairs",
            new StairsBlock(ACID_STONE.getDefaultState(), Block.Settings.copy(ACID_STONE)));

    public static final Block VOLCANIC_ROCK = register("volcanic_rock",
            new Block(Block.Settings.create()
                    .strength(2.5f, 4.5f).sounds(ModSoundGroups.VOLCANIC).requiresTool()));

    public static final Block VOLCANIC_ROCK_SLAB = register("volcanic_rock_slab",
            new SlabBlock(Block.Settings.copy(VOLCANIC_ROCK)));

    public static final Block VOLCANIC_ROCK_STAIRS = register("volcanic_rock_stairs",
            new StairsBlock(VOLCANIC_ROCK.getDefaultState(), Block.Settings.copy(VOLCANIC_ROCK)));

    public static final Block VOLCANIC_ROCK_WALL = register("volcanic_rock_wall",
            new WallBlock(Block.Settings.copy(VOLCANIC_ROCK)));

    public static final Block HARDENED_LAVA = register("hardened_lava",
            new HardenedLavaBlock(Block.Settings.create()
                    .strength(3.0f, 6.0f).sounds(ModSoundGroups.VOLCANIC).requiresTool().luminance(state -> 3)));

    public static final Block SULFUR_BRICKS = register("sulfur_bricks",
            new Block(Block.Settings.create()
                    .strength(2.5f, 3.5f).sounds(ModSoundGroups.SULFUR).requiresTool()));

    public static final Block SULFUR_BRICKS_SLAB = register("sulfur_bricks_slab",
            new SlabBlock(Block.Settings.copy(SULFUR_BRICKS)));

    public static final Block SULFUR_BRICKS_STAIRS = register("sulfur_bricks_stairs",
            new StairsBlock(SULFUR_BRICKS.getDefaultState(), Block.Settings.copy(SULFUR_BRICKS)));

    public static final Block SULFUR_BRICKS_WALL = register("sulfur_bricks_wall",
            new WallBlock(Block.Settings.copy(SULFUR_BRICKS)));

    public static final Block CHISELED_SULFUR_BRICKS = register("chiseled_sulfur_bricks",
            new Block(Block.Settings.copy(SULFUR_BRICKS)));

    public static final Block ASH_BLOCK = register("ash_block",
            new AshBlock(Block.Settings.create().strength(0.5f).sounds(ModSoundGroups.ASH)));

    public static final Block TOXIC_SAND = register("toxic_sand",
            new ToxicSandBlock(Block.Settings.create().strength(0.5f).sounds(BlockSoundGroup.SAND)));

    public static final Block SULFURIC_DIRT = register("sulfuric_dirt",
            new Block(Block.Settings.create().strength(0.5f).sounds(BlockSoundGroup.GRAVEL)));

    public static final Block VOLCANIC_ASH_SOIL = register("volcanic_ash_soil",
            new Block(Block.Settings.create().strength(0.6f).sounds(ModSoundGroups.ASH)));

    public static final Block SULFUR_FLOWER = register("sulfur_flower",
            new SulfurFlowerBlock(Block.Settings.create()
                    .noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).luminance(state -> 5)), false);

    public static final Block TOXIC_GRASS = register("toxic_grass",
            new ToxicGrassBlock(Block.Settings.create()
                    .noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), false);

    public static final Block CRYSTAL_VINE = register("crystal_vine",
            new CrystalVineBlock(Block.Settings.create()
                    .noCollision().strength(0.2f).sounds(ModSoundGroups.CRYSTAL).luminance(state -> 8).nonOpaque()), false);

    public static final Block ASH_BUSH = register("ash_bush",
            new PlantBlock(Block.Settings.create()
                    .noCollision().breakInstantly().sounds(ModSoundGroups.ASH)), false);

    public static final Block GLOW_MUSHROOM = register("glow_mushroom",
            new GlowMushroomBlock(Block.Settings.create()
                    .noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).luminance(state -> 12)), false);

    public static final Block BURNT_LOG     = register("burnt_log",     new PillarBlock(Block.Settings.copy(Blocks.OAK_LOG).sounds(BlockSoundGroup.WOOD)));
    public static final Block BURNT_WOOD    = register("burnt_wood",    new PillarBlock(Block.Settings.copy(Blocks.OAK_WOOD).sounds(BlockSoundGroup.WOOD)));
    public static final Block BURNT_PLANKS  = register("burnt_planks",  new Block(Block.Settings.copy(Blocks.OAK_PLANKS)));
    public static final Block BURNT_LEAVES  = register("burnt_leaves",  new LeavesBlock(Block.Settings.copy(Blocks.OAK_LEAVES)));
    public static final Block BURNT_SAPLING = register("burnt_sapling", new SaplingBlock(new BurntSaplingGenerator(), Block.Settings.copy(Blocks.OAK_SAPLING)), false);

    public static final Block SULFUR_LOG     = register("sulfur_log",     new PillarBlock(Block.Settings.create().strength(2.0f).sounds(ModSoundGroups.SULFUR)));
    public static final Block SULFUR_WOOD    = register("sulfur_wood",    new PillarBlock(Block.Settings.copy(SULFUR_LOG)));
    public static final Block SULFUR_PLANKS  = register("sulfur_planks",  new Block(Block.Settings.copy(SULFUR_LOG)));
    public static final Block SULFUR_LEAVES  = register("sulfur_leaves",  new LeavesBlock(Block.Settings.create().strength(0.2f).sounds(BlockSoundGroup.GRASS).nonOpaque().luminance(s -> 3)));
    public static final Block SULFUR_SAPLING = register("sulfur_sapling", new SaplingBlock(new SulfurSaplingGenerator(), Block.Settings.copy(Blocks.OAK_SAPLING)), false);

    public static final Block CRYSTAL_LOG     = register("crystal_log",     new CrystalLogBlock(Block.Settings.create().strength(3.0f).sounds(ModSoundGroups.CRYSTAL).luminance(s -> 6)));
    public static final Block CRYSTAL_WOOD    = register("crystal_wood",    new CrystalLogBlock(Block.Settings.copy(CRYSTAL_LOG)));
    public static final Block CRYSTAL_PLANKS  = register("crystal_planks",  new Block(Block.Settings.copy(CRYSTAL_LOG)));
    public static final Block CRYSTAL_LEAVES  = register("crystal_leaves",  new LeavesBlock(Block.Settings.create().strength(0.2f).sounds(ModSoundGroups.CRYSTAL).nonOpaque().luminance(s -> 10)));
    public static final Block CRYSTAL_SAPLING = register("crystal_sapling", new SaplingBlock(new CrystalSaplingGenerator(), Block.Settings.copy(Blocks.OAK_SAPLING)), false);

    public static final Block ACID_BLOCK          = register("acid",          new AcidBlock(Block.Settings.create().replaceable().liquid().noCollision().dropsNothing().ticksRandomly()), false);
    public static final Block LIQUID_SULFUR_BLOCK = register("liquid_sulfur", new LiquidSulfurBlock(Block.Settings.create().replaceable().liquid().noCollision().dropsNothing().ticksRandomly().luminance(s -> 7)), false);

    private static Block register(String name, Block block) {
        return register(name, block, true);
    }

    private static Block register(String name, Block block, boolean registerItem) {
        Identifier id = Identifier.of(EvolvingRealms.MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        if (registerItem) {
            Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        }
        return block;
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering blocks for Evolving Realms");
    }
}
