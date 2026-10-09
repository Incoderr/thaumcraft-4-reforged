package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class AlchemicalFurnaceBlock extends Block implements EntityBlock {
    public AlchemicalFurnaceBlock(Properties properties) {
        super(properties);
    }

    public static int lightLevel(BlockState state) {
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemicalFurnaceBlockEntity(pos, state);
    }
}
