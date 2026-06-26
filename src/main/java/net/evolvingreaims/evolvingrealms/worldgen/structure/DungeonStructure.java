package net.evolvingreaims.evolvingrealms.worldgen.structure;

import com.mojang.serialization.MapCodec;
import net.evolvingreaims.evolvingrealms.dungeon.DungeonGenerator;
import net.evolvingreaims.evolvingrealms.dungeon.DungeonTheme;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

/**
 * Dungeon Structure — the world-generation hook that spawns procedural
 * dungeons. Picks a random theme from {@link DungeonTheme} and delegates
 * all generation to {@link DungeonGenerator}.
 */
public class DungeonStructure extends Structure {

    public static final MapCodec<DungeonStructure> CODEC =
            createCodec(DungeonStructure::new);

    public DungeonStructure(Config config) {
        super(config);
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        BlockPos origin = context.chunkPos().getCenterAtY(0);

        // Find surface height at centre
        int surfaceY = context.chunkGenerator().getHeightInGround(
                origin.getX(), origin.getZ(),
                Heightmap.Type.WORLD_SURFACE_WG,
                context.world(), context.noiseConfig());

        if (surfaceY < 20) return Optional.empty();

        // Descend 10–20 blocks underground for the dungeon entrance
        int dungeonY = surfaceY - 15 - context.random().nextInt(10);

        BlockPos dungeonOrigin = new BlockPos(origin.getX(), dungeonY, origin.getZ());

        DungeonTheme[] themes = DungeonTheme.values();
        DungeonTheme theme = themes[context.random().nextInt(themes.length)];

        return Optional.of(new StructurePosition(dungeonOrigin, collector ->
                DungeonGenerator.generate(
                        context.world(),
                        dungeonOrigin,
                        theme,
                        context.random())));
    }

    @Override
    public StructureType<?> getType() {
        return EvolvingRealmsStructureTypes.DUNGEON;
    }
}
