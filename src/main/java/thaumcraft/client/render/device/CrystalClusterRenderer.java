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
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.device.CrystalClusterBlock;
import thaumcraft.device.CrystalClusterBlockEntity;

public class CrystalClusterRenderer implements BlockEntityRenderer<CrystalClusterBlockEntity, CrystalClusterRenderer.State> {
    public static class State extends BlockEntityRenderState {
        int kind;
        Direction facing = Direction.UP;
        long seed;
        int rotation;
        int ticks;
    }

    public CrystalClusterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(CrystalClusterBlockEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        if (entity.getBlockState().getBlock() instanceof CrystalClusterBlock block) {
            state.kind = block.getKind();
            state.facing = entity.getBlockState().getValue(CrystalClusterBlock.FACING);
            state.seed = state.kind + entity.getBlockPos().getX() + entity.getBlockPos().getY() * entity.getBlockPos().getZ();
            state.rotation = Math.floorMod(entity.getBlockPos().hashCode(), 4);
        }
        state.ticks = Minecraft.getInstance().player == null ? 0 : Minecraft.getInstance().player.tickCount;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.kind == CrystalClusterBlock.STRANGE) {
            DeviceRendering.strangeCrystal(collector, poseStack, state.lightCoords, state.facing, state.rotation, state.ticks);
        } else {
            DeviceRendering.crystalCluster(collector, poseStack, state.kind, state.facing, state.seed, state.ticks);
        }
    }
}
