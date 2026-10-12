package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.monster.Wisp;

public final class WispRenderer extends EntityRenderer<Wisp, WispRenderState> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/misc/wisp.png");
    private static final Identifier CORE_TEXTURE = Thaumcraft.id("textures/misc/particles.png");

    public WispRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public WispRenderState createRenderState() {
        return new WispRenderState();
    }

    @Override
    public void extractRenderState(Wisp entity, WispRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.alive = entity.isAlive();
        state.hurt = entity.hurtTime > 0;
        if (entity.getAspect() != null) {
            state.color = entity.getAspect().getColor();
        }
    }

    @Override
    public void submit(WispRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.alive) {
            return;
        }
        int frame = (int) state.ageInTicks & 15;
        float column = frame % 4;
        float row = frame / 4;
        float inset = 0.001F;
        float u0 = (column * 16.0F + inset) / 64.0F;
        float u1 = (column * 16.0F + 16.0F - inset) / 64.0F;
        float v0 = (row * 16.0F + inset) / 64.0F;
        float v1 = (row * 16.0F + 16.0F - inset) / 64.0F;
        int color = state.hurt
            ? 0xFFFF0000 | ((state.color >> 8 & 255) * 255 / 300) << 8 | (state.color & 255) * 255 / 300
            : 0xFF000000 | state.color;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.45F, 0.0F);
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.additive(TEXTURE), u0, u1, v0, v1, 1.0F, color, 240);
        int coreFrame = (int) state.ageInTicks & 15;
        float coreSize = 0.4F + (float) Math.sin(state.ageInTicks / 10.0F) * 0.1F;
        Billboards.submit(
            poseStack,
            collector,
            camera,
            TcRenderTypes.additive(CORE_TEXTURE),
            coreFrame / 16.0F,
            (coreFrame + 1) / 16.0F,
            5.0F / 16.0F,
            6.0F / 16.0F,
            coreSize,
            0xFFFFFFFF,
            240
        );
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
