package thaumcraft.client;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.network.WarpEventPayload;
import thaumcraft.registry.ModEffects;
import thaumcraft.registry.ModSounds;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class WarpEffectClient {
    private static final Identifier DEATH_GAZE_SHADER = Identifier.withDefaultNamespace("desaturatetc");
    private static final Identifier BLURRED_VISION_SHADER = Identifier.withDefaultNamespace("blurtc");
    private static final Identifier HUNGER_SHADER = Identifier.withDefaultNamespace("hunger");
    private static final Identifier SUN_SCORNED_SHADER = Identifier.withDefaultNamespace("sunscorned");
    private static int warpVignetteTicks;
    private static int fogTicks;
    private static @Nullable LocalPlayer lastPlayer;
    private static @Nullable Level lastLevel;

    private WarpEffectClient() {
    }

    public static void handle(WarpEventPayload payload, LocalPlayer player) {
        if (payload.event() == 0) {
            warpVignetteTicks = 100;
            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), ModSounds.HEARTBEAT.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
        } else if (payload.event() == 1) {
            fogTicks = 2400;
        }
    }

    public static int warpVignetteTicks() {
        return warpVignetteTicks;
    }

    public static int fogTicks() {
        return fogTicks;
    }

    @SubscribeEvent
    static void clientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            boolean changedLevel = lastLevel != null && lastLevel != player.level();
            if (lastPlayer != null && (lastPlayer != player || changedLevel)) {
                clearOurPostEffects(lastPlayer);
            }
            if (changedLevel) {
                warpVignetteTicks = 0;
                fogTicks = 0;
            }
            lastPlayer = player;
            lastLevel = player.level();
            syncPostEffects(player);
            if (player.hasEffect(ModEffects.DEATH_GAZE)) {
                warpVignetteTicks = 10;
            }
        }
        if (warpVignetteTicks > 0) {
            warpVignetteTicks--;
        }
        if (fogTicks > 0) {
            fogTicks--;
        }
        if (Minecraft.getInstance().player == null) {
            clearOurPostEffects(lastPlayer);
            lastPlayer = null;
            lastLevel = null;
            warpVignetteTicks = 0;
            fogTicks = 0;
        }
    }

    private static void syncPostEffects(LocalPlayer player) {
        List<Identifier> active = new ArrayList<>(player.getActivePostEffects());
        active.removeIf(WarpEffectClient::isOurPostEffect);
        if (player.hasEffect(ModEffects.DEATH_GAZE)) {
            active.add(DEATH_GAZE_SHADER);
        }
        if (player.hasEffect(ModEffects.BLURRED_VISION)) {
            active.add(BLURRED_VISION_SHADER);
        }
        if (player.hasEffect(ModEffects.UNNATURAL_HUNGER)) {
            active.add(HUNGER_SHADER);
        }
        if (player.hasEffect(ModEffects.SUN_SCORNED)) {
            active.add(SUN_SCORNED_SHADER);
        }
        if (!player.getActivePostEffects().equals(active)) {
            player.setActivePostEffects(active);
        }
    }

    private static void clearOurPostEffects(@Nullable LocalPlayer player) {
        if (player == null) {
            return;
        }
        List<Identifier> active = new ArrayList<>(player.getActivePostEffects());
        if (active.removeIf(WarpEffectClient::isOurPostEffect)) {
            player.setActivePostEffects(active);
        }
    }

    private static boolean isOurPostEffect(Identifier identifier) {
        return identifier.equals(DEATH_GAZE_SHADER)
            || identifier.equals(BLURRED_VISION_SHADER)
            || identifier.equals(HUNGER_SHADER)
            || identifier.equals(SUN_SCORNED_SHADER);
    }
}
