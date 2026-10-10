package thaumcraft.client.render.device;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.device.ArcaneBoreBlockEntity;

public class ArcaneBoreRenderer implements BlockEntityRenderer<ArcaneBoreBlockEntity, ArcaneBoreRenderer.State> {
    public static class State extends BlockEntityRenderState {
        float rotX;
        float rotZ;
        boolean baseDown;
        int topRotation;
        boolean hasFocus;
        float vortexRotation;
    }

    public ArcaneBoreRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ArcaneBoreBlockEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        state.rotX = entity.getRotX() - entity.getVRadX() + partialTicks * entity.getSpeedX();
        state.rotZ = entity.getRotZ() - entity.getVRadZ() + partialTicks * entity.getSpeedZ();
        state.baseDown = entity.getBaseOrientation() == Direction.DOWN;
        state.topRotation = entity.getTopRotation();
        state.hasFocus = entity.hasFocus();
        Minecraft minecraft = Minecraft.getInstance();
        int ticks = minecraft.getCameraEntity() == null ? 0 : minecraft.getCameraEntity().tickCount;
        state.vortexRotation = ticks % 45 + partialTicks;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        BoreRendering.bore(collector, poseStack, state.lightCoords, new BoreRendering.BoreState(state.rotX, state.rotZ, state.baseDown, state.topRotation, state.hasFocus, state.vortexRotation));
    }

    @Override
    public AABB getRenderBoundingBox(ArcaneBoreBlockEntity entity) {
        return new AABB(entity.getBlockPos()).inflate(1.0);
    }
}
