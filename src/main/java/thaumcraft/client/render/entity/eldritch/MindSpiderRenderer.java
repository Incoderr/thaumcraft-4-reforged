package thaumcraft.client.render.entity.eldritch;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.eldritch.MindSpider;

public class MindSpiderRenderer extends SpiderRenderer<MindSpider> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/taint_spider.png");

    public MindSpiderRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.03F;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(MindSpider entity, LivingEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (entity.isHarmless()) {
            String viewer = entity.getViewer();
            state.isInvisible = viewer.isEmpty() || Minecraft.getInstance().player == null
                || !viewer.equals(Minecraft.getInstance().player.getUUID().toString());
        }
    }

    @Override
    protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
        poseStack.scale(0.3F, 0.3F, 0.3F);
    }
}
