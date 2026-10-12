package thaumcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.world.entity.monster.zombie.Zombie;
import thaumcraft.entity.monster.GiantBrainyZombie;

public final class GiantBrainyZombieRenderer extends BrainyZombieRenderer {
    public GiantBrainyZombieRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new GiantBrainyZombieRenderState();
    }

    @Override
    public void extractRenderState(Zombie entity, ZombieRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (entity instanceof GiantBrainyZombie giant && state instanceof GiantBrainyZombieRenderState giantState) {
            giantState.anger = giant.getAnger();
        }
    }

    @Override
    protected void scale(ZombieRenderState state, PoseStack poseStack) {
        if (state instanceof GiantBrainyZombieRenderState giantState) {
            float scale = Math.max(1.0F, giantState.anger);
            poseStack.scale(scale, scale, scale);
        }
    }
}
