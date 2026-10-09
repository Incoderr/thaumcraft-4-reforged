package thaumcraft.research;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ResearchNotes {
    private ResearchNotes() {
    }

    public static ItemStack createNote(ResearchItem item, RandomSource random) {
        return ItemStack.EMPTY;
    }

    public static boolean createNoteForPlayer(ServerPlayer player, String key) {
        return false;
    }

    public static boolean consumeInk(Player player, boolean doit) {
        return false;
    }

    public static int findNoteSlot(Player player, String key) {
        return -1;
    }
}
