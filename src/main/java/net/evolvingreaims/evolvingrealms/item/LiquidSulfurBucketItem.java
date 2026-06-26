package net.evolvingreaims.evolvingrealms.item;

import net.evolvingreaims.evolvingrealms.fluid.LiquidSulfurFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;

/**
 * Bucket containing Liquid Sulfur fluid.
 */
public class LiquidSulfurBucketItem extends BucketItem {

    public LiquidSulfurBucketItem(Settings settings) {
        super(LiquidSulfurFluid.STILL, settings);
    }
}
