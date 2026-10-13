package thaumcraft.client.golem;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import thaumcraft.entity.golem.GolemKeys;

public record GolemAdvancedProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<GolemAdvancedProperty> MAP_CODEC = MapCodec.unit(new GolemAdvancedProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return GolemKeys.snapshot(stack).advanced();
    }

    @Override
    public MapCodec<GolemAdvancedProperty> type() {
        return MAP_CODEC;
    }
}
