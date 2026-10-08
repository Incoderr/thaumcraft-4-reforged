package thaumcraft.wand;

import java.util.List;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;

public record WandCap(
    String tag,
    float baseCostModifier,
    List<Aspect> specialAspects,
    float specialCostModifier,
    Identifier item,
    int craftCost,
    @Nullable String requiredMetal
) {
    public float getCostModifier(Aspect aspect) {
        return specialAspects.contains(aspect) ? specialCostModifier : baseCostModifier;
    }

    public Identifier texture() {
        return Thaumcraft.id("textures/model/wand_cap_" + tag + ".png");
    }

    public String research() {
        return "CAP_" + tag;
    }
}
