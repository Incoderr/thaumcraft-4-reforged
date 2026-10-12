package thaumcraft.client.render.entity.eldritch;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.eldritch.EldritchGuardian;

public class EldritchGuardianRenderer extends MobRenderer<EldritchGuardian, GuardianRenderState, EldritchGuardianModel> {
    private static final Identifier TEXTURE = Thaumcraft.id("textures/model/eldritch_guardian.png");

    public EldritchGuardianRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchGuardianModel(context.bakeLayer(EldritchGuardianModel.LAYER)), 0.5F);
    }

    @Override
    public GuardianRenderState createRenderState() {
        return new GuardianRenderState();
    }

    @Override
    public void extractRenderState(EldritchGuardian entity, GuardianRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.armLiftLeft = entity.armLiftLeft;
        state.armLiftRight = entity.armLiftRight;
    }

    @Override
    public Identifier getTextureLocation(GuardianRenderState state) {
        return TEXTURE;
    }
}
