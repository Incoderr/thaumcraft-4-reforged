package thaumcraft.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

public class TaintBlock extends Block {
    public enum Kind {
        CRUST,
        SOIL,
        FLESH
    }

    private final Kind kind;

    public TaintBlock(Kind kind, Properties properties) {
        super(properties);
        this.kind = kind;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return kind != Kind.FLESH;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (kind == Kind.FLESH) {
            return;
        }
        TaintSpread.tick(level, pos);
        if (kind == Kind.SOIL) {
            if (random.nextInt(10) == 0) {
                level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            }
            return;
        }
        if (tryToFall(level, pos)) {
            return;
        }
    }

    private boolean tryToFall(ServerLevel level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (level.getBlockState(pos.offset(x, y, z)).is(BlockTags.LOGS)) {
                        return false;
                    }
                }
            }
        }
        if (level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
            return false;
        }
        FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 0;
    }
}
