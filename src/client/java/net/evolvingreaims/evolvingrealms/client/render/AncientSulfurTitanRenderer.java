package net.evolvingreaims.evolvingrealms.client.render;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.entity.boss.AncientSulfurTitanEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.WitherEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

/**
 * Ancient Sulfur Titan renderer — uses the Wither model as a placeholder.
 * The texture swaps per boss phase (phase1/phase2/phase3 sub-textures).
 */
public class AncientSulfurTitanRenderer extends MobEntityRenderer<AncientSulfurTitanEntity, WitherEntityModel<AncientSulfurTitanEntity>> {

    private static final Identifier TEXTURE_P1 =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/ancient_sulfur_titan_p1.png");
    private static final Identifier TEXTURE_P2 =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/ancient_sulfur_titan_p2.png");
    private static final Identifier TEXTURE_P3 =
            Identifier.of(EvolvingRealms.MOD_ID, "textures/entity/ancient_sulfur_titan_p3.png");

    public AncientSulfurTitanRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WitherEntityModel<>(ctx.getPart(EntityModelLayers.WITHER)), 3.0f);
    }

    @Override
    public Identifier getTexture(AncientSulfurTitanEntity entity) {
        return switch (entity.getCurrentPhase()) {
            case 2 -> TEXTURE_P2;
            case 3 -> TEXTURE_P3;
            default -> TEXTURE_P1;
        };
    }

    @Override
    public float getShadowRadius(AncientSulfurTitanEntity entity) {
        return 3.5f;
    }
}
