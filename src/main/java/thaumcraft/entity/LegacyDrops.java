package thaumcraft.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModItems;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class LegacyDrops {
    private LegacyDrops() {
    }

    @SubscribeEvent
    static void onDrops(LivingDropsEvent event) {
        var mob = event.getEntity();
        if (!(mob.level() instanceof ServerLevel level)) return;
        boolean zombie = mob instanceof Zombie && !(mob instanceof thaumcraft.entity.monster.BrainyZombie) && event.isRecentlyHit();
        if (!zombie && !(mob instanceof Villager)) return;
        int looting = event.getSource().getEntity() instanceof LivingEntity killer
            ? EnchantmentHelper.getEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), killer) : 0;
        if (mob.getRandom().nextInt(10) - looting < 1) event.getDrops().add(new ItemEntity(level, mob.getX(), mob.getY() + mob.getEyeHeight(), mob.getZ(), new ItemStack(zombie ? ModItems.ZOMBIE_BRAIN.get() : ModItems.GOLD_COIN.get())));
    }
}
