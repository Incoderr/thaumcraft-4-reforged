package thaumcraft.client.render.device;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.device.HungryChestBlock;
import thaumcraft.device.HungryChestBlockEntity;

public class HungryChestRenderer implements BlockEntityRenderer<HungryChestBlockEntity, HungryChestRenderer.State> {
    public static class State extends BlockEntityRenderState {
        Direction facing = Direction.NORTH;
        float lid;
    }

    public HungryChestRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(HungryChestBlockEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        state.facing = entity.getBlockState().getValue(HungryChestBlock.FACING);
        state.lid = Mth.lerp(partialTicks, entity.prevLidAngle, entity.lidAngle);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        WardedRendering.hungryChest(collector, poseStack, state.lightCoords, state.facing, state.lid);
    }
}
