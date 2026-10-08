package thaumcraft.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class NodeBlockEntity extends TcBlockEntity {
    public NodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NODE.get(), pos, state);
    }
}
