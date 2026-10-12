package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.projectile.GolemOrb;

public final class GolemOrbRenderer extends EntityRenderer<GolemOrb, GolemOrbRenderState> {
    public GolemOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public GolemOrbRenderState createRenderState() {
        return new GolemOrbRenderState();
    }

    @Override
    public void extractRenderState(GolemOrb orb, GolemOrbRenderState state, float partialTicks) {
        super.extractRenderState(orb, state, partialTicks);
        state.red = orb.isRed();
    }

    @Override
    public void submit(GolemOrbRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int age = (int) state.ageInTicks;
        float u0 = (1 + age % 6) / 8.0F;
        float v0 = state.red ? 0.75F : 0.875F;
        float size = 1.0F + Mth.sin(age / 5.0F) * 0.2F + 0.2F;
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.additive(ParticleFx.PARTICLES), u0, u0 + 0.125F, v0, v0 + 0.125F, size, 0xCCFFFFFF, 220);
        super.submit(state, poseStack, collector, camera);
    }
}
