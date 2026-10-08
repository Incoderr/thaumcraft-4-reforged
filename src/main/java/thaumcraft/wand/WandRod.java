package thaumcraft.wand;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;

public record WandRod(
    String tag,
    String textureTag,
    int capacity,
    Identifier item,
    int craftCost,
    @Nullable RodUpdate onUpdate,
    boolean glowing,
    boolean staff,
    boolean runes
) {
    public Identifier texture() {
        return Thaumcraft.id("textures/model/wand_rod_" + textureTag + ".png");
    }

    public String research() {
        return "ROD_" + tag;
    }
}
