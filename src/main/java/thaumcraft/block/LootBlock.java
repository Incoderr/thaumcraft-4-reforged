package thaumcraft.block;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class LootBlock extends Block {
    public enum Tier implements StringRepresentable {
        COMMON,
        UNCOMMON,
        RARE;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum Kind {
        URN,
        CRATE
    }

    public static final EnumProperty<Tier> TIER = EnumProperty.create("tier", Tier.class);

    private final Kind kind;

    public LootBlock(Kind kind, Properties properties) {
        super(properties);
        this.kind = kind;
        registerDefaultState(stateDefinition.any().setValue(TIER, Tier.COMMON));
    }

    public Kind kind() {
        return kind;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIER);
    }
}
