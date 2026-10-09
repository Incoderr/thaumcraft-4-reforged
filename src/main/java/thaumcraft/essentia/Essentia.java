package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import org.jspecify.annotations.Nullable;

public final class Essentia {
    private Essentia() {
    }

    public static @Nullable EssentiaTransport getConnectable(BlockGetter level, BlockPos pos, Direction face) {
        return level.getBlockEntity(pos.relative(face)) instanceof EssentiaTransport transport ? transport : null;
    }
}
