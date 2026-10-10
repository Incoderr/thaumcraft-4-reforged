package thaumcraft.client.render.device;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.device.BannerBlockEntity;

public class BannerRenderer implements BlockEntityRenderer<BannerBlockEntity, BannerRenderer.State> {
    public static class State extends BlockEntityRenderState {
        int color = -1;
        @Nullable Aspect aspect;
        int facing;
        boolean onWall;
        float swing;
    }

    public BannerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public AABB getRenderBoundingBox(BannerBlockEntity banner) {
        return new AABB(banner.getBlockPos()).expandTowards(0.0, 1.0, 0.0);
    }

    @Override
    public void extractRenderState(BannerBlockEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        state.color = entity.getColor();
        state.aspect = entity.getAspect();
        state.facing = entity.getFacing();
        state.onWall = entity.isOnWall();
        long time = entity.getLevel() == null ? 0L : entity.getLevel().getGameTime();
        state.swing = WardedRendering.bannerSwing(state.blockPos.getX(), state.blockPos.getY(), state.blockPos.getZ(), time, partialTicks);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        WardedRendering.banner(collector, poseStack, state.lightCoords, state.color, state.aspect, true, state.facing, state.onWall, state.swing);
    }
}
