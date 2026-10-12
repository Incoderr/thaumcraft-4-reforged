package thaumcraft.client.render.entity.taint;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class TaintMobRenderState extends LivingEntityRenderState {
    public float flap;
    public float flapSpeed;
    public float sporeSize;
    public float displaySize;
    public float swelling;
    public float flailIntensity = 1.0F;
    public boolean agitated;
    public boolean tentacleAttacking;
    public boolean sheared;
    public boolean hurt;
}
