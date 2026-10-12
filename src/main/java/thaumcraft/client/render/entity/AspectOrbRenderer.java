package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.AspectOrb;

public final class AspectOrbRenderer extends EntityRenderer<AspectOrb, AspectOrbRenderState> {
    public AspectOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override public AspectOrbRenderState createRenderState() { return new AspectOrbRenderState(); }

    @Override public void extractRenderState(AspectOrb entity, AspectOrbRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.color = entity.getAspect().getColor();
        state.blend = entity.getAspect().getBlend();
        state.age = entity.getAge();
    }

    @Override public void submit(AspectOrbRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int frame = (int) (System.nanoTime() / 25_000_000L % 16L);
        float u0 = frame / 16.0F;
        float u1 = (frame + 1.0F) / 16.0F;
        float scale = 0.1F + 0.3F * Math.max(0.0F, (150.0F - state.age) / 150.0F);
        int tint = net.minecraft.util.ARGB.color(128, state.color);
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.byBlend(ParticleFx.PARTICLES, state.blend, true), u0, u1, 0.5F, 0.5625F, scale, tint, 0xF000F0);
        super.submit(state, poseStack, collector, camera);
    }

}
