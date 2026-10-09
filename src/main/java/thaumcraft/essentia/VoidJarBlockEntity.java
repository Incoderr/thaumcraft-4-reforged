package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.registry.ModBlockEntities;

public class VoidJarBlockEntity extends JarBlockEntity {
    public VoidJarBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.VOID_JAR.get(), pos, state);
    }

    protected VoidJarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
