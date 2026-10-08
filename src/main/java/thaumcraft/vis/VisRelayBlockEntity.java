package thaumcraft.vis;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class VisRelayBlockEntity extends TcBlockEntity {
    public VisRelayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VIS_RELAY.get(), pos, state);
    }
}
