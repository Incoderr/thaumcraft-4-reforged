package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class JarBlock extends Block implements EntityBlock {
    private final boolean voidJar;

    public JarBlock(boolean voidJar, Properties properties) {
        super(properties);
        this.voidJar = voidJar;
    }

    public boolean isVoidJar() {
        return voidJar;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return voidJar ? new VoidJarBlockEntity(pos, state) : new JarBlockEntity(pos, state);
    }
}
