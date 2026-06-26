package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.mob.SulfurSlimeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class SulfurSlimeRenderer extends MobEntityRenderer<SulfurSlimeEntity, SlimeEntityModel<SulfurSlimeEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/sulfur_slime.png");

    public SulfurSlimeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SlimeEntityModel<>(ctx.getPart(EntityModelLayers.SLIME)), 0.25f);
    }

    @Override
    public Identifier getTexture(SulfurSlimeEntity entity) {
        return TEXTURE;
    }
}
