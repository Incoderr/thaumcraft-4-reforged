package thaumcraft.client.platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public final class WarpPostEffects {
    private WarpPostEffects() {
    }

    public static void sync(LocalPlayer player, Collection<Identifier> ownedEffects, Collection<Identifier> activeEffects) {
        List<Identifier> active = new ArrayList<>(player.getActivePostEffects());
        active.removeAll(ownedEffects);
        active.addAll(activeEffects);
        if (!player.getActivePostEffects().equals(active)) {
            player.setActivePostEffects(active);
        }
    }

    public static void clear(@Nullable LocalPlayer player, Collection<Identifier> ownedEffects) {
        if (player == null) {
            return;
        }
        List<Identifier> active = new ArrayList<>(player.getActivePostEffects());
        if (active.removeAll(ownedEffects)) {
            player.setActivePostEffects(active);
        }
    }
}
