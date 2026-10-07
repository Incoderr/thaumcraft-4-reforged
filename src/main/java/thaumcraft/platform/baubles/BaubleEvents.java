package thaumcraft.platform.baubles;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.Baubles;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class BaubleEvents {
    private BaubleEvents() {
    }

    @SubscribeEvent
    static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        BaubleInventory inventory = BaubleAttachments.inventory(player);
        for (int slot = 0; slot < Baubles.slotCount(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.getItem() instanceof Bauble bauble) {
                bauble.onWornTick(stack, player);
            }
        }
    }

    @SubscribeEvent
    static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player && player.level() instanceof ServerLevel level && !level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
            BaubleAttachments.inventory(player).dropAll(event.getDrops());
        }
    }
}
