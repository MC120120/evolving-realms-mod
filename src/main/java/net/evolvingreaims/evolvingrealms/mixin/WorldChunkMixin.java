package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * WorldChunk hook — reserved for future chunk-load dungeon injection.
 * Currently a lightweight observer that does not alter vanilla behaviour.
 */
@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {

    @Inject(method = "setLoadedToWorld", at = @At("TAIL"), require = 0)
    private void evolvingrealms$onChunkLoaded(CallbackInfo ci) {
    }
}
