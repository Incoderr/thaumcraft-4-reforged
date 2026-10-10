package thaumcraft.client.render.device;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.device.BrainJarBlockEntity;

public class BrainJarRenderer implements BlockEntityRenderer<BrainJarBlockEntity, BrainJarRenderer.State> {
    public static class State extends BlockEntityRenderState {
        float rotation;
    }

    public BrainJarRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(BrainJarBlockEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        float difference = entity.rota - entity.rotb;
        while (difference >= 3.141593F) {
            difference -= 6.283185F;
        }
        while (difference < -3.141593F) {
            difference += 6.283185F;
        }
        state.rotation = entity.rotb + difference * partialTicks;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        DeviceRendering.brainJar(collector, poseStack, state.lightCoords, state.rotation, DeviceRendering.viewerTicks());
    }
}
