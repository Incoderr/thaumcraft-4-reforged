package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class AdvancedAlchemicalFurnaceBlock extends Block implements EntityBlock {
    public AdvancedAlchemicalFurnaceBlock(Properties properties) {
        super(properties);
    }

    public static int lightLevel(BlockState state) {
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AdvancedAlchemicalFurnaceBlockEntity(pos, state);
    }
}
