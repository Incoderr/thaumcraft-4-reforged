package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
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
        state.texture = entity.getAspect().getImage();
        state.color = entity.getAspect().getColor();
        state.amount = entity.getAspectValue();
    }

    @Override public void submit(AspectOrbRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        float scale = 0.25F + Math.min(state.amount, 10) * 0.0125F;
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.additive(state.texture), 0.0F, 1.0F, 0.0F, 1.0F, scale, 0xFFFFFFFF, 0xF000F0);
        super.submit(state, poseStack, collector, camera);
    }

}
