package net.evolvingreaims.evolvingrealms.mixin;

import net.evolvingreaims.evolvingrealms.world.LivingEarthManager;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks into ServerWorld.breakBlock to notify the LivingEarthManager
 * when trees are felled by the player (for ecological tracking).
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "breakBlock", at = @At("TAIL"))
    private void evolvingrealms$onBreakBlock(BlockPos pos, boolean drop, net.minecraft.entity.Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        ServerWorld self = (ServerWorld)(Object)this;
        BlockState state = self.getBlockState(pos);
        // Log natural block removals for ecological system (future feature: forest density map)
        // Currently just a hook point for future use
    }
}
