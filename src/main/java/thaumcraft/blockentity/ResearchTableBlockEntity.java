package thaumcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.network.AspectCombinePayload;
import thaumcraft.network.AspectPlacePayload;
import thaumcraft.registry.ModBlockEntities;

public class ResearchTableBlockEntity extends TcBlockEntity {
    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESEARCH_TABLE.get(), pos, state);
    }

    public static void handlePlace(AspectPlacePayload payload, Player player) {
    }

    public static void handleCombine(AspectCombinePayload payload, Player player) {
    }
}
