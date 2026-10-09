package thaumcraft.fx;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface FxProxy {
    default void beamPulse(Level level, Entity source, Entity target, int color) {
    }

    default void nodeBolt(Level level, float x, float y, float z, float targetX, float targetY, float targetZ) {
    }

    default void visDrain(Level level, BlockPos drainer, BlockPos source, int primal) {
    }

    default void blockSparkle(Level level, BlockPos pos, int color, int count) {
    }

    default void crucibleBoil(Level level, BlockPos pos, float fluidHeight, int aspectAmount, int count) {
    }

    default void crucibleBubble(Level level, float x, float y, float z, float red, float green, float blue) {
    }

    default void crucibleFroth(Level level, float x, float y, float z) {
    }

    default void crucibleFrothDown(Level level, float x, float y, float z) {
    }

    default void ventParticles(Level level, double x, double y, double z, double motionX, double motionY, double motionZ, int color, float scale) {
    }

    default void essentiaSource(Level level, BlockPos pos, int dx, int dy, int dz, int color) {
    }

    default void essentiaTrail(Level level, BlockPos from, BlockPos to, int count, int color, float scale) {
    }

    default void infusionItemParticles(Level level, double x, double y, double z, BlockPos target, ItemStack stack) {
    }

    default void infusionBlockParticles(Level level, double x, double y, double z, BlockPos target, BlockState state) {
    }

    default void infusionSparkle(Level level, double x, double y, double z, BlockPos target, boolean alternate) {
    }

    default void blockRunes(Level level, double x, double y, double z, float red, float green, float blue, int duration, float gravity) {
    }
}
