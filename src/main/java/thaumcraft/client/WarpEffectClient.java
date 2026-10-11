package thaumcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.network.WarpEventPayload;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class WarpEffectClient {
    private static final SoundEvent HEARTBEAT = SoundEvent.createVariableRangeEvent(Thaumcraft.id("heartbeat"));
    private static int warpVignetteTicks;
    private static int fogTicks;

    private WarpEffectClient() {
    }

    public static void handle(WarpEventPayload payload, LocalPlayer player) {
        if (payload.event() == 0) {
            warpVignetteTicks = 100;
            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), HEARTBEAT, SoundSource.AMBIENT, 1.0F, 1.0F, false);
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
        if (warpVignetteTicks > 0) {
            warpVignetteTicks--;
        }
        if (fogTicks > 0) {
            fogTicks--;
        }
        if (Minecraft.getInstance().player == null) {
            warpVignetteTicks = 0;
            fogTicks = 0;
        }
    }
}
