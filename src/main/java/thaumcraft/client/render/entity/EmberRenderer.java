package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.projectile.Ember;

public class EmberRenderer extends EntityRenderer<Ember, SpriteRenderState> {
    public EmberRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public SpriteRenderState createRenderState() {
        return new SpriteRenderState();
    }

    @Override
    public void extractRenderState(Ember entity, SpriteRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.progress = (float) entity.tickCount / Math.max(1, entity.getDuration());
    }

    @Override
    public void submit(SpriteRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int frame = (int) (8.0F * state.progress);
        float u0 = (7 + frame) / 16.0F;
        float v0 = 0.5625F;
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.additive(ParticleFx.PARTICLES), u0, u0 + 0.0625F, v0, v0 + 0.0625F, 0.25F + state.progress, 0xE6FFFFFF, 220);
        super.submit(state, poseStack, collector, camera);
    }
}
