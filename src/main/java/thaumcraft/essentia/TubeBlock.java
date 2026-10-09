package thaumcraft.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class TubeBlock extends Block implements EntityBlock {
    public enum Kind {
        TUBE,
        VALVE,
        FILTER,
        RESTRICT,
        ONEWAY
    }

    private final Kind kind;

    public TubeBlock(Kind kind, Properties properties) {
        super(properties);
        this.kind = kind;
    }

    public Kind kind() {
        return kind;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch (kind) {
            case TUBE -> new TubeBlockEntity(pos, state);
            case VALVE -> new ValveTubeBlockEntity(pos, state);
            case FILTER -> new FilterTubeBlockEntity(pos, state);
            case RESTRICT -> new RestrictTubeBlockEntity(pos, state);
            case ONEWAY -> new OnewayTubeBlockEntity(pos, state);
        };
    }
}
