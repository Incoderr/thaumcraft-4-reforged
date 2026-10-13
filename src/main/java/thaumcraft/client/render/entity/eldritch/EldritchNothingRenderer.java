package thaumcraft.client.render.entity.eldritch;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.EldritchSurfaces;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.eldritch.EldritchNothingBlockEntity;

public final class EldritchNothingRenderer implements BlockEntityRenderer<EldritchNothingBlockEntity, EldritchNothingRenderer.State> {
    private static final net.minecraft.resources.Identifier TUNNEL = Thaumcraft.id("textures/misc/tunnel.png");
    private static final net.minecraft.resources.Identifier FIELD = Thaumcraft.id("textures/misc/particlefield.png");
    private static final net.minecraft.resources.Identifier FIELD_FAR = Thaumcraft.id("textures/misc/particlefield32.png");

    static final class State extends BlockEntityRenderState {
        boolean[] exposed = new boolean[6];
        boolean nearby;
        float time;
    }

    public EldritchNothingRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(EldritchNothingBlockEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        state.nearby = cameraPosition.distanceToSqr(Vec3.atCenterOf(entity.getBlockPos())) < 512.0;
        state.time = (entity.getLevel() == null ? 0.0F : (float) entity.getLevel().getGameTime()) + partialTicks;
        if (entity.getLevel() == null || !entity.exposed()) {
            java.util.Arrays.fill(state.exposed, false);
            return;
        }
        BlockPos pos = entity.getBlockPos();
        for (Direction direction : Direction.values()) {
            state.exposed[direction.ordinal()] = !entity.getLevel().getBlockState(pos.relative(direction)).isSolidRender();
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        for (Direction direction : Direction.values()) {
            if (!state.exposed[direction.ordinal()]) {
                continue;
            }
            this.submitFace(state, direction, poseStack, collector);
        }
    }

    private void submitFace(State state, Direction direction, PoseStack poseStack, SubmitNodeCollector collector) {
        if (!state.nearby) {
            EldritchSurfaces.submitFace(collector, poseStack, TcRenderTypes.translucent(FIELD_FAR), direction.ordinal(), 1.0F, 0.0F, 0xFF808080, 0xF000F0);
            return;
        }
        for (int layer = 0; layer < 16; layer++) {
            float layerAlpha = layer == 0 ? 0.1F : 1.0F / (17 - layer);
            float tile = layer == 0 ? 0.125F : 0.5F;
            float rotate = (float) Math.toRadians((layer * layer * 4321.0F + layer * 9.0F) * 2.0F);
            int color = layer == 0 ? 0xFFFFFFFF : colorForLayer(layer, layerAlpha);
            EldritchSurfaces.submitFace(collector, poseStack, layer == 0 ? TcRenderTypes.translucent(TUNNEL) : TcRenderTypes.additive(FIELD), direction.ordinal(), tile, rotate, color, 0xF000F0);
        }
    }

    private static int colorForLayer(int layer, float alpha) {
        java.util.Random random = new java.util.Random(31100L + layer);
        int red = (int) ((random.nextFloat() * 0.5F + 0.1F) * 255.0F * alpha);
        int green = (int) ((random.nextFloat() * 0.5F + 0.4F) * 255.0F * alpha);
        int blue = (int) ((random.nextFloat() * 0.5F + 0.5F) * 255.0F * alpha);
        return net.minecraft.util.ARGB.color(255, red, green, blue);
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(EldritchNothingBlockEntity entity) {
        return new net.minecraft.world.phys.AABB(entity.getBlockPos()).inflate(0.1);
    }
}
