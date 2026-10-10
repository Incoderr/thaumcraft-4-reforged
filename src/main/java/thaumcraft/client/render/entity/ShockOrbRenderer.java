package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;

public class ShockOrbRenderer<T extends Entity> extends EntityRenderer<T, EntityRenderState> {
    public ShockOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int age = (int) state.ageInTicks;
        float u0 = (1 + age % 6) / 8.0F;
        float v0 = 0.875F;
        float bob = Mth.sin(age / 5.0F) * 0.2F + 0.2F;
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.additive(ParticleFx.PARTICLES), u0, u0 + 0.125F, v0, v0 + 0.125F, 1.0F + bob, 0xCCFFFFFF, 220);
        super.submit(state, poseStack, collector, camera);
    }
}
