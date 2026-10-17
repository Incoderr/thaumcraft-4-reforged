package thaumcraft.node;

import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.AspectList;

public interface NodeView {
    String getId();

    AspectList getAspects();

    NodeType getNodeType();

    @Nullable NodeModifier getNodeModifier();
}
