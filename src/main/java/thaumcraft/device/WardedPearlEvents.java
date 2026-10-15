package thaumcraft.device;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import thaumcraft.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class WardedPearlEvents {
    private WardedPearlEvents() {
    }

    @SubscribeEvent
    static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof ThrownEnderpearl pearl)) return;
        for (BlockPos pos : BlockPos.betweenClosed(pearl.blockPosition().offset(-5, -5, -5), pearl.blockPosition().offset(5, 5, 5))) {
            if (OwnedBlockEntity.at(event.getLevel(), pos) != null) {
                if (pearl.getOwner() instanceof Player player) player.sendSystemMessage(Component.literal("§5§oThe magic of a nearby warded object destroys the ender pearl."));
                pearl.discard();
                event.setCanceled(true);
                return;
            }
        }
    }
}
