package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.TcRenderTypes;
import thaumcraft.entity.projectile.PrimalArrow;

public class PrimalArrowRenderer extends ArrowRenderer<PrimalArrow, PrimalArrowRenderState> {
    private static final Identifier ARROW = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");
    private static final Identifier WISP = Thaumcraft.id("textures/misc/wisp.png");
    private static final int[] COLORS = {16777215, 16777086, 16727041, 37119, 40960, 15650047, 5592439};

    public PrimalArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public PrimalArrowRenderState createRenderState() {
        return new PrimalArrowRenderState();
    }

    @Override
    protected Identifier getTextureLocation(PrimalArrowRenderState state) {
        return ARROW;
    }

    @Override
    public void extractRenderState(PrimalArrow entity, PrimalArrowRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.type = entity.getArrowType();
    }

    @Override
    public void submit(PrimalArrowRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        int frame = (int) state.ageInTicks % 16;
        float u0 = frame % 4 / 4.0F;
        float v0 = frame / 4 / 4.0F;
        int color = ARGB.opaque(COLORS[Math.min(state.type, 5) + 1]);
        var renderType = state.type < 5 ? TcRenderTypes.additive(WISP) : TcRenderTypes.translucent(WISP);
        Billboards.submit(poseStack, collector, camera, renderType, u0 + 0.249F, u0, v0 + 0.249F, v0, 1.0F, color, 0xF000F0);
    }
}
