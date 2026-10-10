package thaumcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;

public class WardedBlockEntity extends TcBlockEntity {
    public WardedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WARDED.get(), pos, state);
    }
}
