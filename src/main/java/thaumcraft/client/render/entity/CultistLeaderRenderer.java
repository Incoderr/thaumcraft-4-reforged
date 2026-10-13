package thaumcraft.client.render.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.boss.CultistLeader;

public final class CultistLeaderRenderer extends HumanoidMobRenderer<CultistLeader, CultistRenderState, HumanoidModel<CultistRenderState>> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/cultist.png");

    public CultistLeaderRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
            this,
            ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), HumanoidModel::new),
            context.getEquipmentRenderer()
        ));
    }

    @Override
    public CultistRenderState createRenderState() {
        return new CultistRenderState();
    }

    @Override
    public Identifier getTextureLocation(CultistRenderState state) {
        return TEXTURE;
    }
}
