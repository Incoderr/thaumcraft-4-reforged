package thaumcraft.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import thaumcraft.aspect.AspectKeys;
import thaumcraft.aspect.AspectList;
import thaumcraft.registry.ModItems;

public final class JarLabelRecipe extends CustomRecipe {
    public static final JarLabelRecipe INSTANCE = new JarLabelRecipe();
    public static final RecipeSerializer<JarLabelRecipe> SERIALIZER = new RecipeSerializer<>(MapCodec.unit(INSTANCE), StreamCodec.<RegistryFriendlyByteBuf, JarLabelRecipe>unit(INSTANCE));

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int labels = 0;
        int phials = 0;
        boolean filtered = false;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.is(ModItems.JAR_LABEL.get())) {
                labels++;
                filtered = !AspectKeys.ASPECTS.getOrDefault(stack, AspectList.EMPTY).isEmpty();
            } else if (stack.is(ModItems.ESSENTIA_PHIAL.get()) && AspectKeys.ASPECTS.getOrDefault(stack, AspectList.EMPTY).size() == 1) {
                phials++;
            } else return false;
        }
        return labels == 1 && (phials == 1 || phials == 0 && filtered);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack result = new ItemStack(ModItems.JAR_LABEL.get());
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.is(ModItems.ESSENTIA_PHIAL.get())) {
                AspectList aspects = AspectKeys.ASPECTS.getOrDefault(stack, AspectList.EMPTY);
                if (aspects.size() != 1) return ItemStack.EMPTY;
                AspectKeys.ASPECTS.set(result, new AspectList().add(aspects.getAspects().getFirst(), 0));
            }
        }
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int slot = 0; slot < input.size(); slot++) {
            if (input.getItem(slot).is(ModItems.ESSENTIA_PHIAL.get())) result.set(slot, input.getItem(slot).copyWithCount(1));
        }
        return result;
    }

    @Override
    public RecipeSerializer<JarLabelRecipe> getSerializer() {
        return SERIALIZER;
    }
}
