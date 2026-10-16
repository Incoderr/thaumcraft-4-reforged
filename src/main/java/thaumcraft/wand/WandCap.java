package thaumcraft.wand;

import java.util.List;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;

public record WandCap(
    String tag,
    float baseCostModifier,
    List<Aspect> specialAspects,
    float specialCostModifier,
    Identifier item,
    int craftCost,
    @Nullable String requiredMetal,
    Identifier texture
) {
    public float getCostModifier(Aspect aspect) {
        return specialAspects.contains(aspect) ? specialCostModifier : baseCostModifier;
    }

    public String research() {
        return "CAP_" + tag;
    }
}
