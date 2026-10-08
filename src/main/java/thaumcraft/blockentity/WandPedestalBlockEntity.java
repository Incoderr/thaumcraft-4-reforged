package thaumcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;

public class WandPedestalBlockEntity extends TcBlockEntity {
    public WandPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WAND_PEDESTAL.get(), pos, state);
    }
}
