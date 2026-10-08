package thaumcraft.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class NodeStabilizerBlockEntity extends TcBlockEntity {
    public NodeStabilizerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NODE_STABILIZER.get(), pos, state);
    }
}
