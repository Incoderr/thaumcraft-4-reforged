package thaumcraft.platform;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jspecify.annotations.Nullable;
import thaumcraft.entity.golem.GolemBase;

public final class GolemContainers {
    public interface User extends ContainerUser {
        GolemContainers containers();

        @Override
        default boolean hasContainerOpen(ContainerOpenersCounter counter, BlockPos pos) { return containers().positions.contains(pos); }

        @Override
        default double getContainerInteractionRange() { return 3.0; }
    }

    private final GolemBase golem;
    private @Nullable Container opened;
    private List<BlockPos> positions = List.of();
    private long closeAt;

    public GolemContainers(GolemBase golem) { this.golem = golem; }

    public void open(BlockPos pos) {
        var level = golem.level();
        var state = level.getBlockState(pos);
        Container container;
        List<BlockPos> parts;
        if (state.getBlock() instanceof ChestBlock chest) {
            container = ChestBlock.getContainer(chest, state, level, pos, true);
            parts = state.getValue(ChestBlock.TYPE) == ChestType.SINGLE ? List.of(pos.immutable())
                : List.of(pos.immutable(), ChestBlock.getConnectedBlockPos(pos, state));
        } else {
            container = level.getBlockEntity(pos) instanceof Container candidate ? candidate : null;
            parts = List.of(pos.immutable());
        }
        if (container == null) return;
        closeAt = level.getGameTime() + 5;
        if (opened != null && positions.equals(parts)) return;
        close();
        opened = container;
        positions = parts;
        opened.startOpen(golem);
    }

    public void tick() {
        if (opened != null && (!golem.isWorking() || golem.level().getGameTime() >= closeAt)) close();
    }

    public void close() {
        if (opened == null) return;
        Container container = opened;
        opened = null;
        positions = List.of();
        container.stopOpen(golem);
    }
}
