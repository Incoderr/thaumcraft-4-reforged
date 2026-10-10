package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.projectile.ExplosiveOrb;

public class ExplosiveOrbRenderer extends EntityRenderer<ExplosiveOrb, EntityRenderState> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/misc/particles2.png");

    public ExplosiveOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        float u0 = (int) state.ageInTicks % 4 / 16.0F;
        float v0 = 0.8125F;
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.translucent(TEXTURE), u0, u0 + 0.0625F, v0, v0 + 0.0625F, 0.7F, 0xCCFFFFFF, 220);
        super.submit(state, poseStack, collector, camera);
    }
}
