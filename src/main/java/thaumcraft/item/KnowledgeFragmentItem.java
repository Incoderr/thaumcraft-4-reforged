package thaumcraft.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import thaumcraft.aspect.Aspect;
import thaumcraft.research.ResearchManager;

public class KnowledgeFragmentItem extends Item {
    public KnowledgeFragmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!player.getAbilities().instabuild) {
            player.getItemInHand(hand).shrink(1);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            for (Aspect aspect : Aspect.getPrimalAspects()) {
                ResearchManager.addAspectPool(serverPlayer, aspect, level.getRandom().nextInt(2) + 1);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
