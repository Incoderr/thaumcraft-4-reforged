package thaumcraft.aspect;

import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public final class AspectRegistry {
    public void register(Item item, AspectList aspects) {
        throw new UnsupportedOperationException();
    }

    public boolean exists(Item item) {
        throw new UnsupportedOperationException();
    }

    public @Nullable AspectList getObjectTags(Item item) {
        throw new UnsupportedOperationException();
    }

    public @Nullable AspectList generate(Item item, List<Item> history) {
        throw new UnsupportedOperationException();
    }

    public void registerEntity(EntityType<?> type, AspectList aspects, Object... nbt) {
        throw new UnsupportedOperationException();
    }
}
