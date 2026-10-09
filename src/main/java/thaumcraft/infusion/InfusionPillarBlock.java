package thaumcraft.infusion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class InfusionPillarBlock extends Block implements EntityBlock {
    private final boolean top;

    public InfusionPillarBlock(boolean top, Properties properties) {
        super(properties);
        this.top = top;
    }

    public boolean isTop() {
        return top;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return top ? null : new InfusionPillarBlockEntity(pos, state);
    }
}
