package thaumcraft.fx;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface FxProxy {
    default void beamPulse(Level level, Entity source, Entity target, int color) {
    }
}
