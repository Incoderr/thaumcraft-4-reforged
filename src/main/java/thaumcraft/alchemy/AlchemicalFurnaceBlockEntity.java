package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import thaumcraft.blockentity.TcBlockEntity;
import thaumcraft.registry.ModBlockEntities;

public class AlchemicalFurnaceBlockEntity extends TcBlockEntity {
    public AlchemicalFurnaceBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ALCHEMICAL_FURNACE.get(), pos, state);
    }

    protected AlchemicalFurnaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public @Nullable Container exposedItems(@Nullable Direction side) {
        return null;
    }
}
