package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public final class AlchemyHooks {
    private AlchemyHooks() {
    }

    public static boolean isNitor(BlockGetter level, BlockPos pos, BlockState state) {
        return false;
    }
}
