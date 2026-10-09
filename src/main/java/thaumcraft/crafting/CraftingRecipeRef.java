package thaumcraft.crafting;

import net.minecraft.resources.Identifier;

public record CraftingRecipeRef(String key, String research, Identifier recipe, TcStack result) implements TcRecipe {
}
