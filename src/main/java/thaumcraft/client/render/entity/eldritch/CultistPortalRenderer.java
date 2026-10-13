package thaumcraft.client.render.entity.eldritch;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.boss.CultistPortal;

public final class CultistPortalRenderer extends EntityRenderer<CultistPortal, CultistPortalRenderState> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/misc/cultist_portal.png");

    public CultistPortalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.1F;
    }

    @Override
    public CultistPortalRenderState createRenderState() {
        return new CultistPortalRenderState();
    }

    @Override
    public void extractRenderState(CultistPortal entity, CultistPortalRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.health = entity.getHealth() / entity.getMaxHealth();
        state.pulse = entity.getPulse();
        state.hurtTime = entity.hurtTime;
    }

    @Override
    public void submit(CultistPortalRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        float age = state.ageInTicks;
        int frame = 15 - (int) (System.nanoTime() / 50_000_000L) % 16;
        float u0 = frame / 16.0F;
        float u1 = u0 + 0.0625F;
        float scaleY = 1.5F;
        if (state.hurtTime > 0) {
            float hurtPulse = Mth.sin(state.hurtTime * 72.0F * Mth.DEG_TO_RAD);
            scaleY -= hurtPulse / 4.0F;
            frame += (int) (6.0F * hurtPulse);
            u0 = Math.floorMod(frame, 16) / 16.0F;
            u1 = u0 + 0.0625F;
        }
        if (state.pulse > 0) {
            float pulse = Mth.sin(state.pulse * 36.0F * Mth.DEG_TO_RAD);
            scaleY += pulse / 4.0F;
            frame += (int) (12.0F * pulse);
            u0 = Math.floorMod(frame, 16) / 16.0F;
            u1 = u0 + 0.0625F;
        }
        float wound = (1.0F - state.health) / 3.0F;
        float bob = Mth.sin(age / (5.0F - 12.0F * wound)) * wound + wound;
        float bob2 = Mth.sin(age / (6.0F - 15.0F * wound)) * wound + wound;
        float alpha = 1.0F - bob;
        scaleY -= bob / 4.0F;
        float scale = Math.min(50.0F, age) / 50.0F * 1.3F - bob2 / 3.0F;
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.5F, 0.0F);
        poseStack.scale(1.0F, scaleY / 1.5F, 1.0F);
        Billboards.submit(poseStack, collector, camera, TcRenderTypes.translucent(TEXTURE), u0, u1, 0.0F, 1.0F, Math.max(0.01F, scale), ARGB.color(Mth.clamp((int) (alpha * 255.0F), 0, 255), 255, 255, 255), 0xF000F0);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
