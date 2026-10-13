package thaumcraft.client.render.entity.golem;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;

public final class TravelingTrunkRenderState extends LivingEntityRenderState {
    public final ItemStackRenderState upgradeItem = new ItemStackRenderState();
    public Identifier texture;
    public int upgrade;
    public int anger;
    public float lidOpen;
}
