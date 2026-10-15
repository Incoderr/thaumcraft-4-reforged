package thaumcraft.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.alchemy.AlchemyHooks;
import thaumcraft.entity.TaintedMob;
import thaumcraft.entity.monster.taint.TaintMobFactory;
import thaumcraft.registry.ModEffects;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class TaintPoisonEvents {
    private TaintPoisonEvents() {
    }

    @SubscribeEvent
    static void carriedTaint(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        var player = event.getEntity();
        if (player.level().isClientSide() || player.getType().getTags().anyMatch(net.minecraft.tags.EntityTypeTags.UNDEAD::equals) || player.hasEffect(ModEffects.TAINT_POISON)) return;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            var stack = player.getInventory().getItem(slot);
            if ((stack.is(thaumcraft.registry.ModItems.TAINTED_GOO.get()) || stack.is(thaumcraft.registry.ModItems.TAINT_TENDRIL.get())) && player.getRandom().nextInt(4321) <= stack.getCount()) {
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(ModEffects.TAINT_POISON, 120, 0, false, true));
                player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("tc.taint_item_poison", stack.getHoverName().copy().withStyle(net.minecraft.ChatFormatting.DARK_PURPLE, net.minecraft.ChatFormatting.ITALIC)));
                stack.shrink(1);
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity original = event.getEntity();
        if (!(original.level() instanceof ServerLevel level) || original instanceof TaintedMob || !original.hasEffect(ModEffects.TAINT_POISON)) {
            return;
        }
        Entity replacement = TaintMobFactory.createReplacement(original);
        if (replacement != null) {
            if (level.addFreshEntity(replacement)) {
                original.discard();
                return;
            }
            return;
        }
        int size = (int) (1.0F + Math.min(original.getMaxHealth() / 10.0F, 6.0F));
        if (AlchemyHooks.spawnThaumicSlime(level, new Vec3(original.getX(), original.getY(), original.getZ()), size)) {
            original.discard();
        }
    }
}
