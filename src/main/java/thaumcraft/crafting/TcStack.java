package thaumcraft.crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.AspectKeys;
import thaumcraft.aspect.AspectList;

public record TcStack(Identifier item, int count, @Nullable AspectList aspects) {
    public static TcStack of(String item) {
        return new TcStack(Identifier.parse(item), 1, null);
    }

    public static TcStack of(String item, int count) {
        return new TcStack(Identifier.parse(item), count, null);
    }

    public static TcStack withAspects(String item, int count, AspectList aspects) {
        return new TcStack(Identifier.parse(item), count, aspects);
    }

    public @Nullable Item resolve() {
        Item resolved = BuiltInRegistries.ITEM.getValue(item);
        return resolved == Items.AIR ? null : resolved;
    }

    public boolean exists() {
        return resolve() != null;
    }

    public ItemStack create() {
        Item resolved = resolve();
        if (resolved == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(resolved, count);
        if (aspects != null) {
            AspectKeys.ASPECTS.set(stack, aspects.copy());
        }
        return stack;
    }

    public boolean matches(ItemStack stack) {
        Item resolved = resolve();
        if (resolved == null || !stack.is(resolved)) {
            return false;
        }
        return aspects == null || aspects.equals(AspectKeys.ASPECTS.get(stack));
    }
}
