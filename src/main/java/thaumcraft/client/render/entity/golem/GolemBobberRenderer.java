package thaumcraft.client.render.entity.golem;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import thaumcraft.client.platform.Billboards;
import thaumcraft.client.platform.FishingLines;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.golem.GolemBobber;

public final class GolemBobberRenderer extends EntityRenderer<GolemBobber, GolemBobberRenderState> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/fishing/fishing_hook.png");
    public GolemBobberRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.0F;
    }

    @Override
    public GolemBobberRenderState createRenderState() {
        return new GolemBobberRenderState();
    }

    @Override
    public void extractRenderState(GolemBobber entity, GolemBobberRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.bobberX = Mth.lerp(partialTicks, entity.xo, entity.getX());
        state.bobberY = Mth.lerp(partialTicks, entity.yo, entity.getY());
        state.bobberZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
        var fisher = entity.getFisher();
        state.hasFisher = fisher != null;
        if (fisher != null) {
            float yaw = Mth.rotLerp(partialTicks, fisher.yBodyRotO, fisher.yBodyRot) * Mth.DEG_TO_RAD;
            state.fisherX = Mth.lerp(partialTicks, fisher.xo, fisher.getX()) - Mth.cos(yaw) * 0.25 - Mth.sin(yaw) * 0.7;
            state.fisherY = Mth.lerp(partialTicks, fisher.yo, fisher.getY()) + fisher.getEyeHeight() - 0.4;
            state.fisherZ = Mth.lerp(partialTicks, fisher.zo, fisher.getZ()) - Mth.sin(yaw) * 0.25 + Mth.cos(yaw) * 0.7;
        }
    }

    @Override
    public void submit(GolemBobberRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Billboards.submit(
            poseStack,
            collector,
            camera,
            TcRenderTypes.entityCutoutNoCull(TEXTURE),
            0, 1, 0, 1,
            0.5F,
            0xFFFFFFFF,
            state.lightCoords
        );
        if (state.hasFisher) {
            FishingLines.submit(poseStack, collector, (float) (state.fisherX - state.bobberX),
                (float) (state.fisherY - state.bobberY - 0.25), (float) (state.fisherZ - state.bobberZ));
        }
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    protected boolean affectedByCulling(GolemBobber entity) { return false; }
}
