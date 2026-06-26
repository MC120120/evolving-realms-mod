package net.evolvingreaims.evolvingrealms.mixin.client;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * WorldRenderer mixin — reserved for custom sky rendering in the Sulfur Dimension
 * (yellow/orange sulfurous sky with drifting ash particles).
 *
 * Currently a no-op hook; implementation comes in a future update.
 */
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    // Future: @Inject into renderSky to swap the sky box when in Sulfur Dimension
}
