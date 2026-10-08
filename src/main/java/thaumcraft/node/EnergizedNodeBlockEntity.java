package thaumcraft.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class EnergizedNodeBlockEntity extends TcBlockEntity {
    public EnergizedNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENERGIZED_NODE.get(), pos, state);
    }
}
