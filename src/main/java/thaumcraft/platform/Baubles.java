package thaumcraft.platform;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.platform.baubles.BaubleAttachments;
import thaumcraft.platform.baubles.BaubleInventory;

public final class Baubles {
    private static final BaubleType[] SLOT_TYPES = {BaubleType.AMULET, BaubleType.RING, BaubleType.RING, BaubleType.BELT};

    private Baubles() {
    }

    public static int slotCount() {
        return SLOT_TYPES.length;
    }

    public static BaubleType slotType(int slot) {
        return SLOT_TYPES[slot];
    }

    public static ItemStack get(Player player, int slot) {
        if (slot < 0 || slot >= SLOT_TYPES.length) {
            return ItemStack.EMPTY;
        }
        return BaubleAttachments.inventory(player).getItem(slot);
    }

    public static void set(Player player, int slot, ItemStack stack) {
        if (slot >= 0 && slot < SLOT_TYPES.length) {
            BaubleAttachments.inventory(player).setItem(slot, stack);
        }
    }

    public static List<ItemStack> stacks(Player player) {
        BaubleInventory inventory = BaubleAttachments.inventory(player);
        return List.of(inventory.getItem(0), inventory.getItem(1), inventory.getItem(2), inventory.getItem(3));
    }

    public static ItemStack find(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity instanceof Player player) {
            for (ItemStack stack : stacks(player)) {
                if (!stack.isEmpty() && predicate.test(stack)) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isEquipped(Player player, Item item) {
        return !find(player, stack -> stack.is(item)).isEmpty();
    }
}
