package thaumcraft.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import thaumcraft.aspect.Aspect;
import thaumcraft.essentia.EssentiaKeys;
import thaumcraft.entity.monster.ThaumicSlime;
import thaumcraft.registry.ModBlocks;
import thaumcraft.registry.ModEntities;

public final class AlchemyHooks {
    private AlchemyHooks() {
    }

    public static boolean isNitor(BlockGetter level, BlockPos pos, BlockState state) {
        return state.is(ModBlocks.NITOR_LIGHT.get());
    }

    public static boolean spawnThaumicSlime(ServerLevel level, BlockPos pos, int size) {
        ThaumicSlime slime = ModEntities.THAUMIC_SLIME.get().create(level, EntitySpawnReason.TRIGGERED);
        if (slime == null) {
            return false;
        }
        slime.setSlimeSize(size);
        slime.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        return level.addFreshEntity(slime);
    }

    public static boolean taintFromFlux(ServerLevel level, BlockPos pos) {
        return false;
    }

    public static boolean isThaumicSlime(Entity entity) {
        return entity instanceof ThaumicSlime;
    }

    public static int getSlimeSize(Entity entity) {
        return entity instanceof ThaumicSlime slime ? slime.getSlimeSize() : 0;
    }

    public static void setSlimeSize(Entity entity, int size) {
        if (entity instanceof ThaumicSlime slime) {
            slime.setSlimeSize(size);
        }
    }

    public static @Nullable Aspect getJarFilter(ItemStack jar) {
        return EssentiaKeys.FILTER.get(jar);
    }
}
