package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.SulfurPhantomEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.PhantomEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class SulfurPhantomRenderer extends MobEntityRenderer<SulfurPhantomEntity, PhantomEntityModel<SulfurPhantomEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/sulfur_phantom.png");

    public SulfurPhantomRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PhantomEntityModel<>(ctx.getPart(EntityModelLayers.PHANTOM)), 0.75f);
    }

    @Override
    public Identifier getTexture(SulfurPhantomEntity entity) {
        return TEXTURE;
    }
}
