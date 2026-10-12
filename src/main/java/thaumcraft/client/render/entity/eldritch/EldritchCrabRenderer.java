package thaumcraft.client.render.entity.eldritch;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.monster.eldritch.EldritchCrab;

public final class EldritchCrabRenderer extends MobRenderer<EldritchCrab, EldritchCrabRenderState, EldritchCrabModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/crab.png");
    private static final Identifier OVERLAY = Thaumcraft.id("textures/model/craboverlay.png");

    public EldritchCrabRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchCrabModel(context.bakeLayer(EldritchCrabModel.LAYER)), 1.0F);
    }

    @Override
    public EldritchCrabRenderState createRenderState() {
        return new EldritchCrabRenderState();
    }

    @Override
    public void extractRenderState(EldritchCrab entity, EldritchCrabRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.hasHelm = entity.hasHelm();
    }

    @Override
    public Identifier getTextureLocation(EldritchCrabRenderState state) {
        return TEXTURE;
    }

    @Override
    protected boolean shouldRenderLayers(EldritchCrabRenderState state) {
        return super.shouldRenderLayers(state) && state.hasHelm;
    }

    @Override
    public void submit(EldritchCrabRenderState state, com.mojang.blaze3d.vertex.PoseStack poseStack, SubmitNodeCollector collector, net.minecraft.client.renderer.state.level.CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (state.hasHelm && (!state.isInvisible || state.appearsGlowing())) {
            int overlay = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
            collector.order(1).submitModel(
                this.model,
                state,
                poseStack,
                state.appearsGlowing() && state.isInvisible ? TcRenderTypes.outline(OVERLAY) : TcRenderTypes.entityTranslucent(OVERLAY),
                LightCoordsUtil.pack(200, 0),
                overlay,
                state.outlineColor
            );
        }
    }
}
