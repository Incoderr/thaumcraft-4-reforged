package thaumcraft.wand.focus;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.menu.FocusPouchMenu;
import thaumcraft.platform.Bauble;
import thaumcraft.platform.BaubleType;
import thaumcraft.platform.Menus;

public class FocusPouchItem extends Item implements Bauble {
    public static final int SIZE = 18;

    public FocusPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.BELT;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            Menus.open(serverPlayer, new SimpleMenuProvider(
                (containerId, inventory, ignored) -> new FocusPouchMenu(containerId, inventory, player.getItemInHand(hand)),
                Component.translatable("item.thaumcraft.focus_pouch")
            ));
        }
        return InteractionResult.SUCCESS;
    }

    public static List<ItemStack> getInventory(ItemStack pouch) {
        List<ItemStack> contents = new ArrayList<>(SIZE);
        List<ItemStack> stored = FocusKeys.POUCH.get(pouch);
        for (int slot = 0; slot < SIZE; slot++) {
            ItemStack stack = stored != null && slot < stored.size() ? stored.get(slot) : ItemStack.EMPTY;
            contents.add(stack.getItem() instanceof FocusItem ? stack.copy() : ItemStack.EMPTY);
        }
        return contents;
    }

    public static void setInventory(ItemStack pouch, List<ItemStack> contents) {
        List<ItemStack> stored = new ArrayList<>(SIZE);
        for (int slot = 0; slot < SIZE; slot++) {
            stored.add(slot < contents.size() ? contents.get(slot).copy() : ItemStack.EMPTY);
        }
        FocusKeys.POUCH.set(pouch, stored);
    }
}
