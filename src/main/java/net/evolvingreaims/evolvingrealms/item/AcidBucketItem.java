package net.evolvingreaims.evolvingrealms.item;

import net.evolvingreaims.evolvingrealms.fluid.AcidFluid;
import net.minecraft.item.BucketItem;

/**
 * Bucket containing Acid fluid.
 */
public class AcidBucketItem extends BucketItem {

    public AcidBucketItem(Settings settings) {
        super(AcidFluid.STILL, settings);
    }
}
