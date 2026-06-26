package net.evolvingreaims.evolvingrealms.worldgen.feature;

import com.mojang.serialization.Codec;
import net.evolvingreaims.evolvingrealms.registry.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

/**
 * Sulfur Geode Feature — places a roughly spherical geode structure made of:
 * <ul>
 *   <li>Outer shell: Hardened Lava</li>
 *   <li>Inner layer: Sulfur Block</li>
 *   <li>Core: Crystal Block cluster (hollow air pocket with crystal walls)</li>
 * </ul>
 */
public class SulfurGeodeFeature extends Feature<DefaultFeatureConfig> {

    public SulfurGeodeFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        int outerRadius = 6 + random.nextInt(3);
        int innerRadius = outerRadius - 2;
        int coreRadius = innerRadius - 2;

        // Outer shell — hardened lava
        placeSphere(world, origin, outerRadius, ModBlocks.HARDENED_LAVA.getDefaultState());
        // Inner layer — sulfur
        placeSphere(world, origin, innerRadius, ModBlocks.SULFUR_BLOCK.getDefaultState());
        // Air pocket
        placeSphere(world, origin, coreRadius, Blocks.AIR.getDefaultState());
        // Crystal lining on inner wall
        placeCluster(world, origin, coreRadius, random);

        return true;
    }

    private void placeSphere(StructureWorldAccess world, BlockPos origin, int radius,
                              net.minecraft.block.BlockState state) {
        int r2 = radius * radius;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= r2) {
                        BlockPos pos = origin.add(dx, dy, dz);
                        world.setBlockState(pos, state, 3);
                    }
                }
            }
        }
    }

    private void placeCluster(StructureWorldAccess world, BlockPos origin, int radius, Random random) {
        int r2 = radius * radius;
        int r2inner = (radius - 1) * (radius - 1);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int d2 = dx * dx + dy * dy + dz * dz;
                    if (d2 <= r2 && d2 > r2inner && random.nextFloat() < 0.5f) {
                        BlockPos pos = origin.add(dx, dy, dz);
                        world.setBlockState(pos, ModBlocks.CRYSTAL_BLOCK.getDefaultState(), 3);
                    }
                }
            }
        }
    }
}
