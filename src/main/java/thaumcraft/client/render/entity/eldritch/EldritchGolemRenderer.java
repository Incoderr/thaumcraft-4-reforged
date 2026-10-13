package thaumcraft.client.render.entity.eldritch;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.boss.EldritchGolem;

public final class EldritchGolemRenderer extends MobRenderer<EldritchGolem, EldritchGolemRenderState, EldritchGolemModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/eldritch_golem.png");

    public EldritchGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchGolemModel(context.bakeLayer(EldritchGolemModel.LAYER)), 0.5F);
    }

    @Override
    public EldritchGolemRenderState createRenderState() {
        return new EldritchGolemRenderState();
    }

    @Override
    public void extractRenderState(EldritchGolem entity, EldritchGolemRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.headless = entity.isHeadless();
        state.attackTimer = entity.getAttackTimer();
        state.spawnTimer = entity.getSpawnTimer();
        state.partialTick = partialTicks;
    }

    @Override
    public Identifier getTextureLocation(EldritchGolemRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(EldritchGolemRenderState state, PoseStack poseStack) {
        poseStack.scale(2.15F, 2.15F, 2.15F);
    }
}
