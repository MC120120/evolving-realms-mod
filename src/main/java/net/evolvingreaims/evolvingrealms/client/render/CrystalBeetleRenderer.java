package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.CrystalBeetleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.SilverfishEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

/**
 * Crystal Beetle renderer — uses the Silverfish model as a base.
 */
public class CrystalBeetleRenderer extends MobEntityRenderer<CrystalBeetleEntity, SilverfishEntityModel<CrystalBeetleEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/crystal_beetle.png");

    public CrystalBeetleRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SilverfishEntityModel<>(ctx.getPart(EntityModelLayers.SILVERFISH)), 0.3f);
    }

    @Override
    public Identifier getTexture(CrystalBeetleEntity entity) {
        return TEXTURE;
    }
}
