package thaumcraft.node;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import thaumcraft.node.EnergizedNodeBlockEntity;

public class EnergizedNodeBlock extends Block implements EntityBlock {
    public EnergizedNodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergizedNodeBlockEntity(pos, state);
    }
}
