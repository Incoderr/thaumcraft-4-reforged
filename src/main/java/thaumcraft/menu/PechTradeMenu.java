package thaumcraft.menu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.util.Prediction;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.entity.monster.Pech;
import thaumcraft.item.ManaBeanItem;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModItems;
import thaumcraft.registry.ModMenus;

public class PechTradeMenu extends AbstractContainerMenu {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_START = 1;
    private static final int OUTPUT_END = 5;
    private static final int PLAYER_START = 5;
    private static final int HOTBAR_START = 32;
    private static final int SLOT_END = 41;

    private final Inventory playerInventory;
    private final @Nullable Pech pech;
    private final SimpleContainer tradeInventory;

    public PechTradeMenu(int containerId, Inventory inventory, int pechEntityId) {
        this(containerId, inventory, pechEntityId, new SimpleContainer(5));
    }

    public PechTradeMenu(int containerId, Inventory inventory, int pechEntityId, PechTradeInventory serverInventory) {
        this(containerId, inventory, pechEntityId, (SimpleContainer) serverInventory);
    }

    private PechTradeMenu(int containerId, Inventory inventory, int pechEntityId, SimpleContainer tradeInventory) {
        super(ModMenus.PECH_TRADE.get(), containerId);
        this.playerInventory = inventory;
        this.pech = inventory.player.level().getEntity(pechEntityId) instanceof Pech entity ? entity : null;
        this.tradeInventory = tradeInventory;
        if (this.pech != null) {
            this.pech.setTrading(true);
        }
        addSlot(new Slot(tradeInventory, INPUT_SLOT, 36, 29));
        for (int index = 0; index < 4; index++) {
            addSlot(new OutputSlot(tradeInventory, OUTPUT_START + index, 106 + index % 2 * 18, 20 + index / 2 * 18));
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }

    public @Nullable Pech pech() {
        return pech;
    }

    public boolean canTrade() {
        if (pech == null || !pech.isTamed() || tradeInventory.getItem(INPUT_SLOT).isEmpty() || !pech.isValued(tradeInventory.getItem(INPUT_SLOT))) {
            return false;
        }
        for (int slot = OUTPUT_START; slot < OUTPUT_END; slot++) {
            if (!tradeInventory.getItem(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int button) {
        if (button != 0 || !(player instanceof net.minecraft.server.level.ServerPlayer) || this.pech == null || !this.stillValid(player)) {
            return false;
        }
        if (!this.canTrade()) {
            return false;
        }
        ItemStack input = tradeInventory.getItem(INPUT_SLOT);
        int value = pech.getValue(input);
        if (player.getRandom().nextInt(100) <= value / 2) {
            pech.setTamed(false);
        }
        if (player.getRandom().nextInt(5) == 0) {
            value += player.getRandom().nextInt(3);
        } else if (player.getRandom().nextBoolean()) {
            value -= player.getRandom().nextInt(3);
        }
        List<ItemStack> outputs = new ArrayList<>();
        while (value > 0 && outputs.size() < 4) {
            int amount = Math.min(5, Math.max((value + 1) / 2, player.getRandom().nextInt(value) + 1));
            value -= amount;
            ItemStack output = this.chooseOutput(amount, player);
            if (output.isEmpty()) {
                value += amount;
                break;
            }
            outputs.add(output);
        }
        if (outputs.isEmpty()) {
            return false;
        }
        input.shrink(1);
        for (int index = 0; index < outputs.size(); index++) {
            tradeInventory.setItem(OUTPUT_START + index, outputs.get(index));
        }
        if (this.pech.level() instanceof net.minecraft.server.level.ServerLevel level) {
            level.playSound(null, pech.blockPosition(), thaumcraft.registry.ModSounds.PECH_TRADE.get(), net.minecraft.sounds.SoundSource.NEUTRAL, 0.4F, 1.0F);
        }
        return true;
    }

    private ItemStack chooseOutput(int value, Player player) {
        if (value == 1 && player.getRandom().nextBoolean()) {
            List<Integer> occupied = new ArrayList<>();
            for (int index = 0; index < 9; index++) {
                if (!pech.getLoot(index).isEmpty()) {
                    occupied.add(index);
                }
            }
            if (!occupied.isEmpty()) {
                int slot = occupied.get(player.getRandom().nextInt(occupied.size()));
                ItemStack stored = pech.getLoot(slot);
                ItemStack output = stored.copyWithCount(1);
                stored.shrink(1);
                if (stored.isEmpty()) {
                    pech.setLoot(slot, ItemStack.EMPTY);
                }
                return output;
            }
        }
        List<ItemStack> offers = this.offers(value);
        return offers.isEmpty() ? ItemStack.EMPTY : offers.get(player.getRandom().nextInt(offers.size()));
    }

    private List<ItemStack> offers(int value) {
        List<ItemStack> result = new ArrayList<>();
        switch (pech.getPechType()) {
            case NORMAL -> {
                switch (value) {
                    case 1 -> {
                        result.add(new ItemStack(ModBlocks.MANA_BEAN.get()));
                        result.add(new ItemStack(ModItems.COPPER_NUGGET.get()));
                        result.add(new ItemStack(ModItems.TIN_NUGGET.get()));
                        result.add(new ItemStack(ModItems.SILVER_NUGGET.get()));
                        result.add(new ItemStack(ModItems.LEAD_NUGGET.get()));
                    }
                    case 2 -> {
                        result.add(new ItemStack(Items.BLAZE_ROD));
                        result.add(new ItemStack(Items.GOLDEN_APPLE));
                        result.add(new ItemStack(ModItems.AMBER.get()));
                    }
                    case 3 -> {
                        result.add(new ItemStack(Items.EXPERIENCE_BOTTLE));
                        result.add(new ItemStack(ModItems.QUICKSILVER.get()));
                    }
                    case 4 -> result.add(new ItemStack(ModItems.THAUMIUM_PICKAXE.get()));
                    case 5 -> result.add(new ItemStack(ModItems.THAUMIUM_INGOT.get(), 2));
                }
            }
            case WARRIOR -> {
                switch (value) {
                    case 1 -> {
                        result.add(new ItemStack(ModBlocks.MANA_BEAN.get()));
                        result.add(new ItemStack(ModItems.AIR_SHARD.get()));
                        result.add(new ItemStack(ModItems.FIRE_SHARD.get()));
                        result.add(new ItemStack(ModItems.WATER_SHARD.get()));
                        result.add(new ItemStack(ModItems.EARTH_SHARD.get()));
                        result.add(new ItemStack(ModItems.ORDER_SHARD.get()));
                        result.add(new ItemStack(ModItems.ENTROPY_SHARD.get()));
                    }
                    case 2 -> {
                        result.add(new ItemStack(ModItems.AMBER.get()));
                        result.add(new ItemStack(ModItems.THAUMIUM_NUGGET.get()));
                    }
                    case 3 -> {
                        result.add(new ItemStack(Items.GOLDEN_APPLE));
                        result.add(new ItemStack(ModItems.THAUMIUM_INGOT.get()));
                    }
                    case 4 -> result.add(new ItemStack(ModItems.THAUMIUM_SWORD.get()));
                    case 5 -> {
                        result.add(new ItemStack(ModItems.FOCUS_POUCH.get()));
                        result.add(new ItemStack(ModItems.VIS_AMULET.get()));
                    }
                }
            }
            case ARCHER -> {
                switch (value) {
                    case 1 -> result.add(new ItemStack(ModBlocks.MANA_BEAN.get()));
                    case 2 -> {
                        result.add(new ItemStack(Items.GHAST_TEAR));
                        result.add(new ItemStack(Items.ARROW, 8));
                    }
                    case 3 -> {
                        result.add(new ItemStack(Items.EXPERIENCE_BOTTLE));
                        result.add(new ItemStack(Items.GOLDEN_APPLE));
                    }
                    case 4 -> result.add(new ItemStack(ModItems.THAUMIUM_BOOTS.get()));
                    case 5 -> {
                        result.add(new ItemStack(ModItems.FOCUS_PECH.get()));
                        result.add(new ItemStack(ModItems.VIS_AMULET.get()));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return pech != null && pech.isAlive() && pech.isTamed() && player.distanceToSqr(pech) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) {
            return ItemStack.EMPTY;
        }
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < PLAYER_START) {
            if (!moveItemStackTo(stack, PLAYER_START, SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return original;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            for (int index = 0; index < OUTPUT_END; index++) {
                ItemStack stack = tradeInventory.removeItemNoUpdate(index);
                if (!stack.isEmpty()) {
                    player.drop(stack, false, Prediction.PREDICTED);
                }
            }
            if (pech != null) {
                pech.setTrading(false);
            }
        }
    }

    private static final class OutputSlot extends Slot {
        private OutputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
