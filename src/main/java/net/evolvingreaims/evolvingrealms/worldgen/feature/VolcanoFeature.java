package net.evolvingreaims.evolvingrealms.worldgen.feature;

import com.mojang.serialization.Codec;
import net.evolvingreaims.evolvingrealms.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

/**
 * Volcano Feature — generates a volcanic cone in the Sulfur Dimension:
 * <ul>
 *   <li>Conical Hardened-Lava body rising 20-40 blocks above origin.</li>
 *   <li>Crater at the top filled with a lava pool.</li>
 *   <li>Random lava streams flowing down the slopes.</li>
 *   <li>Sulfur ore veins embedded in the flanks.</li>
 * </ul>
 */
public class VolcanoFeature extends Feature<DefaultFeatureConfig> {

    public VolcanoFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        int height = 20 + random.nextInt(20);
        int baseRadius = height / 2 + 4;

        // Build the cone
        for (int y = 0; y <= height; y++) {
            float fraction = 1.0f - (float) y / height;
            int radius = (int) (baseRadius * fraction);

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dz * dz <= radius * radius) {
                        BlockPos pos = origin.add(dx, y, dz);
                        BlockState state;

                        if (y == 0) {
                            state = ModBlocks.HARDENED_LAVA.getDefaultState();
                        } else if (random.nextFloat() < 0.08f) {
                            state = ModBlocks.SULFUR_ORE.getDefaultState();
                        } else if (random.nextFloat() < 0.05f) {
                            state = ModBlocks.VOLCANIUM_ORE.getDefaultState();
                        } else {
                            state = ModBlocks.HARDENED_LAVA.getDefaultState();
                        }

                        world.setBlockState(pos, state, 3);
                    }
                }
            }
        }

        // Crater — hollow top 3 layers, fill with lava
        int craterRadius = 4 + random.nextInt(3);
        for (int dy = -2; dy <= 0; dy++) {
            for (int dx = -craterRadius; dx <= craterRadius; dx++) {
                for (int dz = -craterRadius; dz <= craterRadius; dz++) {
                    if (dx * dx + dz * dz <= craterRadius * craterRadius) {
                        BlockPos pos = origin.add(dx, height + dy, dz);
                        world.setBlockState(pos, dy == 0
                                ? Blocks.LAVA.getDefaultState()
                                : Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }

        // Lava streams down one or two sides
        int streams = 1 + random.nextInt(2);
        for (int s = 0; s < streams; s++) {
            int angle = random.nextBetween(0, 359);
            double rad = Math.toRadians(angle);
            for (int y = height - 2; y >= 0; y--) {
                float fraction = 1.0f - (float) y / height;
                int dist = (int) (baseRadius * fraction * 0.5f);
                int lx = (int) (Math.cos(rad) * dist);
                int lz = (int) (Math.sin(rad) * dist);
                BlockPos streamPos = origin.add(lx, y + 1, lz);
                if (world.getBlockState(streamPos).isAir()) {
                    world.setBlockState(streamPos, Blocks.LAVA.getDefaultState(), 3);
                }
            }
        }

        return true;
    }
}
