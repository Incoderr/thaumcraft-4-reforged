package thaumcraft.blockentity;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jspecify.annotations.Nullable;
import thaumcraft.registry.ModBlockEntities;

public class WardedBlockEntity extends TcBlockEntity {
    public static final ModelProperty<BlockState> DISGUISE = new ModelProperty<>();

    private BlockState disguise = Blocks.STONE.defaultBlockState();
    private byte light;
    private @Nullable UUID owner;

    public WardedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WARDED.get(), pos, state);
    }

    public void ward(BlockState disguise, int light, UUID owner) {
        this.disguise = disguise;
        this.light = (byte) light;
        this.owner = owner;
        sync();
    }

    public BlockState getDisguise() {
        return disguise;
    }

    public int getLight() {
        return light;
    }

    public boolean isOwner(UUID player) {
        return player.equals(owner);
    }

    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(DISGUISE, disguise).build();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        BlockState previous = disguise;
        disguise = input.read("disguise", BlockState.CODEC).orElse(Blocks.STONE.defaultBlockState());
        light = input.getByteOr("ll", (byte) 0);
        owner = input.read("owner", UUIDUtil.CODEC).orElse(null);
        if (level != null && level.isClientSide() && previous != disguise) {
            requestModelDataUpdate();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 8);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("disguise", BlockState.CODEC, disguise);
        output.putByte("ll", light);
        output.storeNullable("owner", UUIDUtil.CODEC, owner);
    }
}
