package thaumcraft.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import thaumcraft.item.armor.RobeArmorItem;

public class RobeArmorDyeRecipe extends ArmorDyeRecipe {
    public static final RobeArmorDyeRecipe INSTANCE = new RobeArmorDyeRecipe();
    public static final MapCodec<RobeArmorDyeRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, RobeArmorDyeRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<RobeArmorDyeRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    protected boolean isTarget(ItemStack stack) {
        return stack.getItem() instanceof RobeArmorItem;
    }

    @Override
    public RecipeSerializer<RobeArmorDyeRecipe> getSerializer() {
        return SERIALIZER;
    }
}
