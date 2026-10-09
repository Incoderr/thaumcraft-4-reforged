package thaumcraft.research;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public final class ResearchCategory {
    private final String key;
    private final Identifier icon;
    private final Identifier background;
    private final Map<String, ResearchItem> research = new LinkedHashMap<>();
    private int minColumn;
    private int minRow;
    private int maxColumn;
    private int maxRow;

    ResearchCategory(String key, Identifier icon, Identifier background) {
        this.key = key;
        this.icon = icon;
        this.background = background;
    }

    public String key() {
        return key;
    }

    public Identifier icon() {
        return icon;
    }

    public Identifier background() {
        return background;
    }

    public String nameKey() {
        return "tc.research_category." + key;
    }

    public Collection<ResearchItem> research() {
        return Collections.unmodifiableCollection(research.values());
    }

    public @Nullable ResearchItem get(String researchKey) {
        return research.get(researchKey);
    }

    public int minColumn() {
        return minColumn;
    }

    public int minRow() {
        return minRow;
    }

    public int maxColumn() {
        return maxColumn;
    }

    public int maxRow() {
        return maxRow;
    }

    boolean add(ResearchItem item) {
        if (research.containsKey(item.key())) {
            return false;
        }
        if (!item.isVirtual()) {
            for (ResearchItem other : research.values()) {
                if (other.column() == item.column() && other.row() == item.row()) {
                    return false;
                }
            }
        }
        research.put(item.key(), item);
        minColumn = Math.min(minColumn, item.column());
        minRow = Math.min(minRow, item.row());
        maxColumn = Math.max(maxColumn, item.column());
        maxRow = Math.max(maxRow, item.row());
        return true;
    }
}
