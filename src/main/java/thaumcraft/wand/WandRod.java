package thaumcraft.wand;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public record WandRod(
    String tag,
    String textureTag,
    int capacity,
    Identifier item,
    int craftCost,
    @Nullable RodUpdate onUpdate,
    boolean glowing,
    boolean staff,
    boolean runes,
    Identifier texture
) {
    public String research() {
        return "ROD_" + tag;
    }
}
