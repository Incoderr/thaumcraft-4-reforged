package thaumcraft.client.fx;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import thaumcraft.client.platform.WorldFxRenderer;
import thaumcraft.fx.FxProxy;

public final class ClientFx implements FxProxy {
    @Override
    public void beamPulse(Level level, Entity source, Entity target, int color) {
        BeamFx beam = new BeamFx(
            level,
            source.getX(),
            source.getY() + source.getEyeHeight(),
            source.getZ(),
            target,
            (color >> 16 & 255) / 255.0F,
            (color >> 8 & 255) / 255.0F,
            (color & 255) / 255.0F,
            20
        );
        beam.setBlendMode(771);
        beam.setWidth(2.5F);
        beam.setType(1);
        beam.setReverse(true);
        beam.setPulse(true);
        WorldFxRenderer.add(beam);
    }
}
