package thaumcraft.client.golem;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import thaumcraft.entity.golem.GolemKeys;

public record GolemHasCoreProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<GolemHasCoreProperty> MAP_CODEC = MapCodec.unit(new GolemHasCoreProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return GolemKeys.snapshot(stack).core() >= 0;
    }

    @Override
    public MapCodec<GolemHasCoreProperty> type() {
        return MAP_CODEC;
    }
}
