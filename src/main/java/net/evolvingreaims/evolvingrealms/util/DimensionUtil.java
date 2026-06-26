package net.evolvingreaims.evolvingrealms.util;

import net.evolvingreaims.evolvingrealms.registry.ModDimensions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/**
 * Utility helpers for dimension checks used across the mod.
 */
public final class DimensionUtil {

    private DimensionUtil() {}

    public static boolean isInSulfurDimension(World world) {
        return world.getRegistryKey().equals(ModDimensions.SULFUR_DIMENSION_KEY);
    }

    public static boolean isInOverworld(World world) {
        return world.getRegistryKey().equals(World.OVERWORLD);
    }

    public static boolean isInNether(World world) {
        return world.getRegistryKey().equals(World.NETHER);
    }

    /**
     * Returns a scaling factor (0.0–2.0) that multiplies mob stats in the
     * Sulfur Dimension (2×) vs Overworld (1×) vs Nether (1.3×).
     */
    public static float getDimensionDifficultyScale(World world) {
        if (isInSulfurDimension(world)) return 2.0f;
        if (isInNether(world)) return 1.3f;
        return 1.0f;
    }
}
