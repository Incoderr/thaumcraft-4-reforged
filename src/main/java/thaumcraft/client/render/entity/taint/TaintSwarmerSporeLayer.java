package thaumcraft.client.render.entity.taint;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;

public class TaintSwarmerSporeLayer extends RenderLayer<TaintMobRenderState, TaintEntityModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/taint_spore.png");
    private static final int FULL_BRIGHT = 0xF000F0;
    private final TaintSwarmerSporeModel model;

    public TaintSwarmerSporeLayer(RenderLayerParent<TaintMobRenderState, TaintEntityModel> renderer, TaintSwarmerSporeModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, TaintMobRenderState state, float yRot, float xRot) {
        renderColoredCutoutModel(this.model, TEXTURE, poseStack, collector, FULL_BRIGHT, state, 0xFFFFFFFF, 0);
    }
}
