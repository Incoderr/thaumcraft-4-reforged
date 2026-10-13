package thaumcraft.client.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import thaumcraft.client.geom.EldritchSurfaceGeometry;

public final class EldritchSurfaces {
    private EldritchSurfaces() {
    }

    public static void submitFace(SubmitNodeCollector collector, PoseStack poseStack, RenderType renderType, int side, float uvScale, float rotation, int color, int light) {
        collector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> EldritchSurfaceGeometry.cubeFace(new EntityQuadSink(pose, buffer), side, uvScale, rotation, color, light));
    }

    public static void submitRectangle(SubmitNodeCollector collector, PoseStack poseStack, RenderType renderType, float x0, float y0, float z0, float x1, float y1, float z1, float uvScale, float rotation, int color, int light) {
        collector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> EldritchSurfaceGeometry.rectangle(new EntityQuadSink(pose, buffer), x0, y0, z0, x1, y1, z1, uvScale, rotation, color, light));
    }
}
