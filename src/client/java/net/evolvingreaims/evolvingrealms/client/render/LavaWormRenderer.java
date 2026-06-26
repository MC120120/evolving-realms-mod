package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.LavaWormEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

/**
 * Uses the Ghast model as a stand-in for the tentacled worm body.
 * Replace with a custom model when artwork is ready.
 */
public class LavaWormRenderer extends MobEntityRenderer<LavaWormEntity, GhastEntityModel<LavaWormEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/lava_worm.png");

    public LavaWormRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GhastEntityModel<>(ctx.getPart(EntityModelLayers.GHAST)), 2.5f);
    }

    @Override
    public Identifier getTexture(LavaWormEntity entity) {
        return TEXTURE;
    }
}
