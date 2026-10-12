package thaumcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.Identifier;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.Pech;

public final class PechRenderer extends MobRenderer<Pech, PechRenderState, PechModel> {
    private static final Identifier[] TEXTURES = {
        Thaumcraft.id("textures/model/pech_forage.png"),
        Thaumcraft.id("textures/model/pech_thaum.png"),
        Thaumcraft.id("textures/model/pech_stalker.png")
    };

    public PechRenderer(EntityRendererProvider.Context context) {
        super(context, new PechModel(context.bakeLayer(PechModel.LAYER)), 0.35F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public PechRenderState createRenderState() {
        return new PechRenderState();
    }

    @Override
    public void extractRenderState(Pech entity, PechRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, itemModelResolver);
        state.pechType = entity.getPechType().ordinal();
        state.mumble = entity.getMumble();
    }

    @Override
    public Identifier getTextureLocation(PechRenderState state) {
        return TEXTURES[Math.max(0, Math.min(TEXTURES.length - 1, state.pechType))];
    }
}
