package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.CrystalGolemEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class CrystalGolemRenderer extends MobEntityRenderer<CrystalGolemEntity, IronGolemEntityModel<CrystalGolemEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/crystal_golem.png");

    public CrystalGolemRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new IronGolemEntityModel<>(ctx.getPart(EntityModelLayers.IRON_GOLEM)), 0.7f);
    }

    @Override
    public Identifier getTexture(CrystalGolemEntity entity) {
        return TEXTURE;
    }
}
