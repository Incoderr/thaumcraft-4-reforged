package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.ThaumicSlime;

public final class ThaumicSlimeRenderer extends MobRenderer<ThaumicSlime, SlimeRenderState, SlimeModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/tslime.png");

    public ThaumicSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.addLayer(new ThaumicSlimeOuterLayer(this, context.getModelSet()));
    }

    @Override
    protected void scale(SlimeRenderState state, PoseStack poseStack) {
        float size = Mth.sqrt(state.size);
        float squish = state.squish / (size * 0.25F + 1.0F);
        float inverse = 1.0F / (squish + 1.0F);
        poseStack.scale(inverse * size + 0.1F, 1.0F / inverse * size + 0.1F, inverse * size + 0.1F);
    }

    @Override
    protected float getShadowRadius(SlimeRenderState state) {
        return Mth.sqrt(state.size) * 0.25F;
    }

    @Override
    public Identifier getTextureLocation(SlimeRenderState state) {
        return TEXTURE;
    }

    @Override
    public SlimeRenderState createRenderState() {
        return new SlimeRenderState();
    }

    @Override
    public void extractRenderState(ThaumicSlime entity, SlimeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.squish = Mth.lerp(partialTicks, entity.prevSquishFactor, entity.squishFactor);
        state.size = entity.getSlimeSize();
    }
}
