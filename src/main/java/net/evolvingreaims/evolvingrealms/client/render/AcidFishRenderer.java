package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.AcidFishEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CodEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class AcidFishRenderer extends MobEntityRenderer<AcidFishEntity, CodEntityModel<AcidFishEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/acid_fish.png");

    public AcidFishRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CodEntityModel<>(ctx.getPart(EntityModelLayers.COD)), 0.3f);
    }

    @Override
    public Identifier getTexture(AcidFishEntity entity) {
        return TEXTURE;
    }
}
