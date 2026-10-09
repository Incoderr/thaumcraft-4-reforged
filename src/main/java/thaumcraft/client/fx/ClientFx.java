package thaumcraft.client.fx;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import thaumcraft.aspect.Aspect;
import thaumcraft.client.geom.LightningBolt;
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

    @Override
    public void nodeBolt(Level level, float x, float y, float z, float targetX, float targetY, float targetZ) {
        BoltRenderer.add(new LightningBolt(x, y, z, targetX, targetY, targetZ, level.getRandom().nextLong(), 10, 4.0F, 5).setType(0).defaultFractal());
    }

    @Override
    public void visDrain(Level level, BlockPos drainer, BlockPos source, int primal) {
        List<Aspect> primals = Aspect.getPrimalAspects();
        if (primal < 0 || primal >= primals.size()) {
            return;
        }
        RandomSource random = level.getRandom();
        WorldFxRenderer.add(new VisSparkleFx(
            random,
            source.getX() + 0.4 + random.nextFloat() * 0.2F,
            source.getY() + 0.4 + random.nextFloat() * 0.2F,
            source.getZ() + 0.4 + random.nextFloat() * 0.2F,
            drainer.getX() + random.nextFloat(),
            drainer.getY() + random.nextFloat(),
            drainer.getZ() + random.nextFloat(),
            primals.get(primal).getColor()
        ));
    }
}
