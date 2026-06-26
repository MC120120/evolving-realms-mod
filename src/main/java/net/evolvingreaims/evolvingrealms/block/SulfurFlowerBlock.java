package net.evolvingreaims.evolvingrealms.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * Sulfur Flower — emits a soft glow and applies Nausea when used in suspicious stew.
 */
public class SulfurFlowerBlock extends FlowerBlock {

    public SulfurFlowerBlock(Settings settings) {
        super(StatusEffects.NAUSEA, 8, settings);
    }
}
