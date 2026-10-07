package thaumcraft.platform;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public final class Transfer {
    private Transfer() {
    }

    private static @Nullable ResourceHandler<ItemResource> items(Level level, BlockPos pos, @Nullable Direction side) {
        return level.getCapability(Capabilities.Item.BLOCK, pos, side);
    }

    private static @Nullable ResourceHandler<FluidResource> fluids(Level level, BlockPos pos, @Nullable Direction side) {
        return level.getCapability(Capabilities.Fluid.BLOCK, pos, side);
    }

    public static boolean hasItems(Level level, BlockPos pos, @Nullable Direction side) {
        return items(level, pos, side) != null;
    }

    public static boolean hasFluids(Level level, BlockPos pos, @Nullable Direction side) {
        return fluids(level, pos, side) != null;
    }

    public static int count(Level level, BlockPos pos, @Nullable Direction side, Predicate<ItemStack> filter) {
        ResourceHandler<ItemResource> handler = items(level, pos, side);
        if (handler == null) {
            return 0;
        }
        int total = 0;
        for (int index = 0; index < handler.size(); index++) {
            ItemResource resource = handler.getResource(index);
            if (!resource.isEmpty() && filter.test(resource.toStack(1))) {
                total += handler.getAmountAsInt(index);
            }
        }
        return total;
    }

    public static ItemStack insert(Level level, BlockPos pos, @Nullable Direction side, ItemStack stack, boolean simulate) {
        ResourceHandler<ItemResource> handler = items(level, pos, side);
        if (handler == null || stack.isEmpty()) {
            return stack;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = ResourceHandlerUtil.insertStacking(handler, ItemResource.of(stack), stack.getCount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return stack.copyWithCount(stack.getCount() - inserted);
        }
    }

    public static ItemStack extract(Level level, BlockPos pos, @Nullable Direction side, Predicate<ItemStack> filter, int maxAmount, boolean simulate) {
        ResourceHandler<ItemResource> handler = items(level, pos, side);
        if (handler == null || maxAmount <= 0) {
            return ItemStack.EMPTY;
        }
        for (int index = 0; index < handler.size(); index++) {
            ItemResource resource = handler.getResource(index);
            if (resource.isEmpty() || !filter.test(resource.toStack(1))) {
                continue;
            }
            try (Transaction transaction = Transaction.openRoot()) {
                int extracted = handler.extract(resource, maxAmount, transaction);
                if (extracted <= 0) {
                    continue;
                }
                if (!simulate) {
                    transaction.commit();
                }
                return resource.toStack(extracted);
            }
        }
        return ItemStack.EMPTY;
    }

    public static int countFluid(Level level, BlockPos pos, @Nullable Direction side, Predicate<FluidStack> filter) {
        ResourceHandler<FluidResource> handler = fluids(level, pos, side);
        if (handler == null) {
            return 0;
        }
        int total = 0;
        for (int index = 0; index < handler.size(); index++) {
            FluidResource resource = handler.getResource(index);
            if (!resource.isEmpty() && filter.test(resource.toStack(1))) {
                total += handler.getAmountAsInt(index);
            }
        }
        return total;
    }

    public static FluidStack insertFluid(Level level, BlockPos pos, @Nullable Direction side, FluidStack stack, boolean simulate) {
        ResourceHandler<FluidResource> handler = fluids(level, pos, side);
        if (handler == null || stack.isEmpty()) {
            return stack;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = handler.insert(FluidResource.of(stack), stack.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return stack.copyWithAmount(stack.getAmount() - inserted);
        }
    }

    public static FluidStack extractFluid(Level level, BlockPos pos, @Nullable Direction side, Predicate<FluidStack> filter, int maxAmount, boolean simulate) {
        ResourceHandler<FluidResource> handler = fluids(level, pos, side);
        if (handler == null || maxAmount <= 0) {
            return FluidStack.EMPTY;
        }
        for (int index = 0; index < handler.size(); index++) {
            FluidResource resource = handler.getResource(index);
            if (resource.isEmpty() || !filter.test(resource.toStack(1))) {
                continue;
            }
            try (Transaction transaction = Transaction.openRoot()) {
                int extracted = handler.extract(resource, maxAmount, transaction);
                if (extracted <= 0) {
                    continue;
                }
                if (!simulate) {
                    transaction.commit();
                }
                return resource.toStack(extracted);
            }
        }
        return FluidStack.EMPTY;
    }
}
