package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.minecraft.world.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.math.ChunkPos;

/**
 * Hook into ChunkGenerator to track chunk generation for dungeon placement.
 * Dungeon injection is handled lazily to avoid TPS spikes during generation.
 */
@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

    @Inject(method = "getDebugHudText", at = @At("HEAD"), cancellable = false, require = 0)
    private void evolvingrealms$onGetDebugText(CallbackInfoReturnable<String> cir) {
    }
}
