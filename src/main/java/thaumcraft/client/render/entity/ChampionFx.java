package thaumcraft.client.render.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.entity.monster.ChampionModifier;
import net.minecraft.world.entity.Mob;

/** Client-only counterpart to TC4 ChampionModifier.showFX. */
@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ChampionFx {
    private static final ParticleOptions[] FX = {
        ParticleTypes.PORTAL,             // bold
        ParticleTypes.CRIT,               // spine
        ParticleTypes.CLOUD,              // armor
        ParticleTypes.EXPLOSION,          // mighty
        ParticleTypes.SOUL,               // grim
        ParticleTypes.ENCHANT,            // warded
        ParticleTypes.REVERSE_PORTAL,     // warp
        ParticleTypes.HAPPY_VILLAGER,     // undying
        ParticleTypes.FLAME,              // fiery
        ParticleTypes.SPORE_BLOSSOM_AIR,  // sickly
        ParticleTypes.WITCH,              // venomous
        ParticleTypes.DAMAGE_INDICATOR,   // vampiric
        ParticleTypes.ITEM_SLIME          // infested
    };

    private ChampionFx() {}

    @SubscribeEvent
    static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Mob mob) || !mob.level().isClientSide() || mob.tickCount % 2 != 0) return;
        int id = ChampionModifier.get(mob);
        if (id < 0 || id >= FX.length || mob.getRandom().nextInt(id == 8 ? 4 : 2) != 0) return;
        double x = mob.getX() + (mob.getRandom().nextDouble() - 0.5) * mob.getBbWidth();
        double y = mob.getY() + mob.getRandom().nextDouble() * mob.getBbHeight();
        double z = mob.getZ() + (mob.getRandom().nextDouble() - 0.5) * mob.getBbWidth();
        double speed = id == 0 || id == 6 ? 0.015 : 0.0;
        mob.level().addParticle(FX[id], x, y, z, (mob.getRandom().nextDouble() - 0.5) * speed, 0.01, (mob.getRandom().nextDouble() - 0.5) * speed);
    }
}
