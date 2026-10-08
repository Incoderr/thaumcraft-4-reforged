package thaumcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Random;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import thaumcraft.world.gen.HilltopStonesGenerator;
import thaumcraft.world.gen.MoundGenerator;
import thaumcraft.world.tree.GreatwoodTreeGenerator;
import thaumcraft.world.tree.SilverwoodTreeGenerator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.aspect.Aspect;
import thaumcraft.aspect.AspectHelper;
import thaumcraft.aspect.AspectList;
import thaumcraft.network.BeamPulsePayload;
import thaumcraft.platform.ModNetwork;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class ThaumcraftCommand {
    private static final int DEFAULT_BEAM_COLOR = 0xFF00FF;

    private ThaumcraftCommand() {
    }

    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher(), event.getBuildContext());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(
            Commands.literal(Thaumcraft.MODID)
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(
                    Commands.literal("fx")
                        .then(
                            Commands.literal("beam")
                                .then(
                                    Commands.argument("target", EntityArgument.entity())
                                        .executes(context -> beam(context, DEFAULT_BEAM_COLOR))
                                        .then(
                                            Commands.argument("color", IntegerArgumentType.integer(0, 0xFFFFFF))
                                                .executes(context -> beam(context, IntegerArgumentType.getInteger(context, "color")))
                                        )
                                )
                        )
                )
                .then(
                    Commands.literal("worldgen")
                        .then(Commands.literal("mound").executes(context -> worldgen(context, "mound")))
                        .then(Commands.literal("hilltop").executes(context -> worldgen(context, "hilltop")))
                        .then(Commands.literal("greatwood").executes(context -> worldgen(context, "greatwood")))
                        .then(Commands.literal("silverwood").executes(context -> worldgen(context, "silverwood")))
                )
                .then(
                    Commands.literal("aspects")
                        .then(Commands.literal("hand").executes(ThaumcraftCommand::aspectsHand))
                        .then(
                            Commands.literal("item")
                                .then(Commands.argument("item", ItemArgument.item(buildContext)).executes(ThaumcraftCommand::aspectsItem))
                        )
                        .then(
                            Commands.literal("entity")
                                .then(Commands.argument("target", EntityArgument.entity()).executes(ThaumcraftCommand::aspectsEntity))
                        )
                )
        );
    }

    private static int worldgen(CommandContext<CommandSourceStack> context, String kind) {
        ServerLevel level = context.getSource().getLevel();
        BlockPos pos = BlockPos.containing(context.getSource().getPosition());
        Random random = new Random(level.getRandom().nextLong());
        boolean placed = switch (kind) {
            case "mound" -> MoundGenerator.generate(level, random, pos.getX(), pos.getY() - 9, pos.getZ());
            case "hilltop" -> HilltopStonesGenerator.generate(level, random, pos.getX(), pos.getY(), pos.getZ());
            case "greatwood" -> new GreatwoodTreeGenerator(false).generate(level, random, pos, random.nextInt(8) == 0);
            default -> new SilverwoodTreeGenerator(false, 7, 4).generate(level, random, pos);
        };
        if (placed) {
            context.getSource().sendSuccess(() -> Component.literal("Generated " + kind), true);
            return 1;
        }
        context.getSource().sendFailure(Component.literal("Cannot generate " + kind + " here"));
        return 0;
    }

    private static int aspectsHand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.isEmpty()) {
            context.getSource().sendFailure(Component.literal("Main hand is empty"));
            return 0;
        }
        return print(context, AspectHelper.getBonusTags(stack, AspectHelper.getObjectTags(stack)));
    }

    private static int aspectsItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack stack = ItemArgument.getItem(context, "item").createItemStack(1);
        return print(context, AspectHelper.getBonusTags(stack, AspectHelper.getObjectTags(stack)));
    }

    private static int aspectsEntity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");
        AspectList aspects = AspectHelper.getEntityTags(target);
        return print(context, aspects == null ? new AspectList() : aspects);
    }

    private static int print(CommandContext<CommandSourceStack> context, AspectList aspects) {
        if (aspects.size() == 0) {
            context.getSource().sendSuccess(() -> Component.literal("No aspects"), false);
            return 0;
        }
        for (Aspect aspect : aspects.getAspectsSortedAmount()) {
            String line = aspect.getName() + " x" + aspects.getAmount(aspect);
            context.getSource().sendSuccess(() -> Component.literal(line), false);
        }
        return aspects.size();
    }

    private static int beam(CommandContext<CommandSourceStack> context, int color) throws CommandSyntaxException {
        Entity source = context.getSource().getEntityOrException();
        Entity target = EntityArgument.getEntity(context, "target");
        ModNetwork.sendToTrackingAndSelf(source, new BeamPulsePayload(source.getId(), target.getId(), color));
        return 1;
    }
}
