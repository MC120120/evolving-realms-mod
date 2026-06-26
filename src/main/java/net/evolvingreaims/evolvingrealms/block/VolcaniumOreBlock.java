package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Volcanium Ore block — rare, drops raw volcanium + large XP.
 */
public class VolcaniumOreBlock extends ExperienceDroppingBlock {

    public VolcaniumOreBlock(Settings settings) {
        super(UniformIntProvider.create(4, 9), settings);
    }
}
