package thaumcraft.entity.monster;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import thaumcraft.Config;
import thaumcraft.Thaumcraft;
import thaumcraft.registry.ModEntities;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class SpawnToggles {
    private SpawnToggles() {
    }

    public static boolean isEnabled(EntityType<?> type) {
        if (type == ModEntities.BRAINY_ZOMBIE.get() || type == ModEntities.GIANT_BRAINY_ZOMBIE.get()) {
            return Config.spawnAngryZombie();
        }
        if (type == ModEntities.FIREBAT.get()) {
            return Config.spawnFireBat();
        }
        if (type == ModEntities.WISP.get()) {
            return Config.spawnWisp();
        }
        if (type == ModEntities.TAINTACLE.get()) {
            return Config.spawnTaintacle();
        }
        if (type == ModEntities.PECH.get()) {
            return Config.spawnPech();
        }
        if (type == ModEntities.ELDRITCH_GUARDIAN.get()) {
            return Config.spawnElder();
        }
        return true;
    }

    @SubscribeEvent
    static void onPositionCheck(MobSpawnEvent.PositionCheck event) {
        EntitySpawnReason reason = event.getSpawnType();
        if ((reason == EntitySpawnReason.NATURAL || reason == EntitySpawnReason.CHUNK_GENERATION) && !isEnabled(event.getEntity().getType())) {
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }
}
