package thaumcraft.client.render.entity.eldritch;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import thaumcraft.client.fx.ParticleFx;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.projectile.EldritchOrb;

public class EldritchOrbRenderer extends EntityRenderer<EldritchOrb, EntityRenderState> {
    public EldritchOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int frame = (int) state.ageInTicks % 4;
        float u = frame / 16.0F;
        float v = 0.8125F;
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.additive(ParticleFx.PARTICLES), u, u + 0.0625F, v, v + 0.0625F, 0.3F, 0xFFFFFFFF, 0xF000F0);
        super.submit(state, poseStack, collector, camera);
    }
}
