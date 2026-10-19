package thaumcraft.client.rei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.ItemStack;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.AspectList;

final class TcReiDisplay extends BasicDisplay {
    static final CategoryIdentifier<TcReiDisplay> CRUCIBLE = CategoryIdentifier.of(Thaumcraft.id("crucible"));
    static final CategoryIdentifier<TcReiDisplay> ARCANE = CategoryIdentifier.of(Thaumcraft.id("arcane"));
    static final CategoryIdentifier<TcReiDisplay> INFUSION = CategoryIdentifier.of(Thaumcraft.id("infusion"));
    static final CategoryIdentifier<TcReiDisplay> ENCHANTMENT = CategoryIdentifier.of(Thaumcraft.id("infusion_enchantment"));

    enum Kind {
        CRUCIBLE,
        ARCANE,
        INFUSION,
        ENCHANTMENT
    }

    private final CategoryIdentifier<TcReiDisplay> category;
    private final Kind kind;
    private final AspectList aspects;
    private final int instability;
    private final int gridWidth;
    private final int gridHeight;

    TcReiDisplay(
        CategoryIdentifier<TcReiDisplay> category,
        Kind kind,
        List<EntryIngredient> inputs,
        List<EntryIngredient> outputs,
        AspectList aspects,
        int instability,
        int gridWidth,
        int gridHeight
    ) {
        super(inputs, outputs);
        this.category = category;
        this.kind = kind;
        this.aspects = aspects.copy();
        this.instability = instability;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return category;
    }

    Kind kind() {
        return kind;
    }

    AspectList aspects() {
        return aspects;
    }

    int instability() {
        return instability;
    }

    int gridWidth() {
        return gridWidth;
    }

    int gridHeight() {
        return gridHeight;
    }

    static EntryIngredient ingredient(List<ItemStack> stacks) {
        if (stacks.isEmpty()) {
            return EntryIngredient.empty();
        }
        EntryIngredient.Builder builder = EntryIngredient.builder(stacks.size());
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                builder.add(EntryStacks.of(stack));
            }
        }
        return builder.build();
    }

    static EntryIngredient ingredient(ItemStack stack) {
        return stack.isEmpty() ? EntryIngredient.empty() : EntryIngredient.of(EntryStacks.of(stack));
    }

    static EntryIngredient ingredientChoices(Collection<ItemStack> stacks) {
        return ingredient(new ArrayList<>(stacks));
    }
}
