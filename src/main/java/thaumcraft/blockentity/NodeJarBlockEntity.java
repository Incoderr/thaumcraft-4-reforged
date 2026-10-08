package thaumcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;

public class NodeJarBlockEntity extends TcBlockEntity {
    public NodeJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NODE_JAR.get(), pos, state);
    }
}
