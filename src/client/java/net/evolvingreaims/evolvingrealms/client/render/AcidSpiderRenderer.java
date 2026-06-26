package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.AcidSpiderEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.SpiderEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class AcidSpiderRenderer extends MobEntityRenderer<AcidSpiderEntity, SpiderEntityModel<AcidSpiderEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/acid_spider.png");

    public AcidSpiderRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SpiderEntityModel<>(ctx.getPart(EntityModelLayers.SPIDER)), 0.8f);
    }

    @Override
    public Identifier getTexture(AcidSpiderEntity entity) {
        return TEXTURE;
    }
}
