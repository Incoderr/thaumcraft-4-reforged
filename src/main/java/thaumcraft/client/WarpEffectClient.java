package thaumcraft.client;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import thaumcraft.Thaumcraft;
import thaumcraft.client.platform.WarpPostEffects;
import thaumcraft.network.WarpEventPayload;
import thaumcraft.registry.ModEffects;
import thaumcraft.registry.ModSounds;

@EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class WarpEffectClient {
    private static final Identifier DEATH_GAZE_SHADER = Thaumcraft.id("desaturatetc");
    private static final Identifier BLURRED_VISION_SHADER = Thaumcraft.id("blurtc");
    private static final Identifier HUNGER_SHADER = Thaumcraft.id("hunger");
    private static final Identifier SUN_SCORNED_SHADER = Thaumcraft.id("sunscorned");
    private static final Identifier WARP_VIGNETTE = Identifier.fromNamespaceAndPath(Thaumcraft.MODID, "textures/misc/vignette.png");
    private static final List<Identifier> OWNED_SHADERS = List.of(DEATH_GAZE_SHADER, BLURRED_VISION_SHADER, HUNGER_SHADER, SUN_SCORNED_SHADER);
    private static int warpVignetteTicks;
    private static int fogTicks;
    private static float fogTarget;
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
        } else if (payload.event() == 2) {
            fogTicks = Math.max(fogTicks, 200);
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
                WarpPostEffects.clear(lastPlayer, OWNED_SHADERS);
            }
            if (changedLevel) {
                warpVignetteTicks = 0;
                fogTicks = 0;
                fogTarget = 0.0F;
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
            if (fogTicks < 100) {
                fogTarget = 0.1F * fogTicks / 100.0F;
            } else if (fogTarget < 0.1F) {
                fogTarget = Math.min(0.1F, fogTarget + 0.001F);
            }
            fogTicks--;
        } else {
            fogTarget = 0.0F;
        }
        if (Minecraft.getInstance().player == null) {
            WarpPostEffects.clear(lastPlayer, OWNED_SHADERS);
            lastPlayer = null;
            lastLevel = null;
            warpVignetteTicks = 0;
            fogTicks = 0;
            fogTarget = 0.0F;
        }
    }

    @SubscribeEvent
    static void renderWarpVignette(RenderGuiLayerEvent.Post event) {
        if (warpVignetteTicks <= 0 || !event.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS)) {
            return;
        }
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        int alpha = Math.min(255, warpVignetteTicks * 255 / 20);
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            WARP_VIGNETTE,
            0,
            0,
            0.0F,
            0.0F,
            graphics.guiWidth(),
            graphics.guiHeight(),
            graphics.guiWidth(),
            graphics.guiHeight(),
            0x00FFFFFF | (alpha << 24)
        );
    }

    @SubscribeEvent
    static void renderWarpFog(ViewportEvent.RenderFog event) {
        if (fogTarget > 0.0F && Minecraft.getInstance().player != null) {
            event.scaleFarPlaneDistance(1.0F - fogTarget * 8.0F);
        }
    }

    private static void syncPostEffects(LocalPlayer player) {
        List<Identifier> active = new ArrayList<>(4);
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
        WarpPostEffects.sync(player, OWNED_SHADERS, active);
    }
}
