package net.evolvingreaims.evolvingrealms.mixin.client;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Client-side GameRenderer mixin — reserved for future post-processing
 * effects in the Sulfur Dimension (heat shimmer, toxic haze overlay).
 *
 * Currently a no-op hook; implementation comes in a future update.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    // Future: @Inject into renderWorld to apply dimension-specific shaders
}
