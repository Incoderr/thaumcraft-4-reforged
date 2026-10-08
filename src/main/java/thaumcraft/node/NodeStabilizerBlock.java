package thaumcraft.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class NodeStabilizerBlock extends Block implements EntityBlock {
    private final boolean advanced;

    public NodeStabilizerBlock(boolean advanced, Properties properties) {
        super(properties);
        this.advanced = advanced;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NodeStabilizerBlockEntity(pos, state);
    }
}
