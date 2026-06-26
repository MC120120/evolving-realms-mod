package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.BurningWolfEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class BurningWolfRenderer extends MobEntityRenderer<BurningWolfEntity, WolfEntityModel<BurningWolfEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/burning_wolf.png");

    public BurningWolfRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WolfEntityModel<>(ctx.getPart(EntityModelLayers.WOLF)), 0.5f);
    }

    @Override
    public Identifier getTexture(BurningWolfEntity entity) {
        return TEXTURE;
    }
}
