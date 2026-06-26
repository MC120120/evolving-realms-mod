package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Sulfur Ore block — drops raw sulfur and experience when mined.
 */
public class SulfurOreBlock extends ExperienceDroppingBlock {

    public SulfurOreBlock(Settings settings) {
        super(UniformIntProvider.create(2, 5), settings);
    }
}
