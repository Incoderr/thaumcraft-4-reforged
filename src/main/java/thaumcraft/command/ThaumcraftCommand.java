package thaumcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.node.Node;
import thaumcraft.node.NodeBlockEntity;
import thaumcraft.node.NodeGenerator;
import thaumcraft.node.NodeModifier;
import thaumcraft.node.NodeType;
import thaumcraft.wand.WandCap;
import thaumcraft.wand.WandRegistry;
import thaumcraft.wand.WandRod;
import thaumcraft.wand.Wands;
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
                .then(
                    Commands.literal("node")
                        .then(
                            Commands.literal("place")
                                .then(
                                    Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> nodePlace(context, null, null))
                                        .then(
                                            Commands.argument("type", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(nodeTypeNames(), builder))
                                                .executes(context -> nodePlace(context, StringArgumentType.getString(context, "type"), null))
                                                .then(
                                                    Commands.argument("modifier", StringArgumentType.word())
                                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(nodeModifierNames(), builder))
                                                        .executes(context -> nodePlace(
                                                            context,
                                                            StringArgumentType.getString(context, "type"),
                                                            StringArgumentType.getString(context, "modifier")
                                                        ))
                                                )
                                        )
                                )
                        )
                        .then(
                            Commands.literal("info")
                                .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ThaumcraftCommand::nodeInfo))
                        )
                )
                .then(
                    Commands.literal("wand")
                        .then(
                            Commands.literal("fill")
                                .executes(context -> wandFill(context, List.of(context.getSource().getPlayerOrException())))
                                .then(
                                    Commands.argument("targets", EntityArgument.players())
                                        .executes(context -> wandFill(context, EntityArgument.getPlayers(context, "targets")))
                                )
                        )
                        .then(
                            Commands.literal("give")
                                .then(
                                    Commands.argument("targets", EntityArgument.players())
                                        .then(
                                            Commands.argument("cap", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                    WandRegistry.caps().stream().map(WandCap::tag),
                                                    builder
                                                ))
                                                .then(
                                                    Commands.argument("rod", StringArgumentType.word())
                                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                            WandRegistry.rods().stream().map(WandRod::tag),
                                                            builder
                                                        ))
                                                        .executes(context -> wandGive(context, false))
                                                        .then(
                                                            Commands.argument("sceptre", BoolArgumentType.bool())
                                                                .executes(context -> wandGive(context, BoolArgumentType.getBool(context, "sceptre")))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static Stream<String> nodeTypeNames() {
        return Stream.of(NodeType.values()).map(NodeType::getSerializedName);
    }

    private static Stream<String> nodeModifierNames() {
        return Stream.concat(Stream.of("none"), Stream.of(NodeModifier.values()).map(NodeModifier::getSerializedName));
    }

    private static int nodePlace(CommandContext<CommandSourceStack> context, String typeName, String modifierName) throws CommandSyntaxException {
        ServerLevel level = context.getSource().getLevel();
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        NodeType type = null;
        if (typeName != null) {
            for (NodeType candidate : NodeType.values()) {
                if (candidate.getSerializedName().equals(typeName)) {
                    type = candidate;
                }
            }
            if (type == null) {
                context.getSource().sendFailure(Component.translatable("commands.thaumcraft.node.unknown_type", typeName));
                return 0;
            }
        }
        NodeModifier modifier = null;
        boolean overrideModifier = modifierName != null;
        if (overrideModifier) {
            for (NodeModifier candidate : NodeModifier.values()) {
                if (candidate.getSerializedName().equals(modifierName)) {
                    modifier = candidate;
                }
            }
            if (modifier == null && !modifierName.equals("none")) {
                context.getSource().sendFailure(Component.translatable("commands.thaumcraft.node.unknown_modifier", modifierName));
                return 0;
            }
        }
        NodeGenerator.createRandomNodeAt(level, pos, level.getRandom(), false, false, false);
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof Node node)) {
            context.getSource().sendFailure(Component.translatable("commands.thaumcraft.node.missing", pos.getX(), pos.getY(), pos.getZ()));
            return 0;
        }
        if (type != null) {
            node.setNodeType(type);
        }
        if (overrideModifier) {
            node.setNodeModifier(modifier);
        }
        entity.setChanged();
        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        Component placedType = Component.translatable(node.getNodeType().getTranslationKey());
        context.getSource().sendSuccess(
            () -> Component.translatable("commands.thaumcraft.node.placed", pos.getX(), pos.getY(), pos.getZ(), placedType),
            true
        );
        return 1;
    }

    private static int nodeInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level = context.getSource().getLevel();
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        if (!(level.getBlockEntity(pos) instanceof NodeBlockEntity node)) {
            context.getSource().sendFailure(Component.translatable("commands.thaumcraft.node.missing", pos.getX(), pos.getY(), pos.getZ()));
            return 0;
        }
        NodeModifier modifier = node.getNodeModifier();
        Component modifierName = modifier == null ? Component.literal("-") : Component.translatable(modifier.getTranslationKey());
        context.getSource().sendSuccess(
            () -> Component.translatable(
                "commands.thaumcraft.node.info",
                Component.translatable(node.getNodeType().getTranslationKey()),
                modifierName,
                describe(node.getAspects()),
                describe(node.getAspectsBase())
            ),
            false
        );
        return 1;
    }

    private static String describe(AspectList aspects) {
        if (aspects.size() == 0) {
            return "-";
        }
        StringBuilder builder = new StringBuilder();
        for (Aspect aspect : aspects.getAspectsSortedAmount()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(aspect.getName()).append(" x").append(aspects.getAmount(aspect));
        }
        return builder.toString();
    }

    private static int wandFill(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets) {
        int filled = 0;
        for (ServerPlayer target : targets) {
            ItemStack stack = target.getItemInHand(InteractionHand.MAIN_HAND);
            if (Wands.isWand(stack)) {
                Wands.fill(stack);
                filled++;
            }
        }
        if (filled == 0) {
            context.getSource().sendFailure(Component.translatable("commands.thaumcraft.wand.none"));
            return 0;
        }
        int count = filled;
        context.getSource().sendSuccess(() -> Component.translatable("commands.thaumcraft.wand.filled", count), true);
        return filled;
    }

    private static int wandGive(CommandContext<CommandSourceStack> context, boolean sceptre) throws CommandSyntaxException {
        String capName = StringArgumentType.getString(context, "cap");
        String rodName = StringArgumentType.getString(context, "rod");
        WandCap cap = WandRegistry.findCap(capName);
        WandRod rod = WandRegistry.findRod(rodName);
        if (cap == null || rod == null) {
            context.getSource().sendFailure(Component.translatable("commands.thaumcraft.wand.unknown", cap == null ? capName : rodName));
            return 0;
        }
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        ItemStack display = Wands.create(cap, rod, sceptre);
        for (ServerPlayer target : targets) {
            ItemStack stack = Wands.create(cap, rod, sceptre);
            Wands.fill(stack);
            if (!target.getInventory().add(stack)) {
                target.drop(stack, false, Prediction.SERVER_ONLY);
            }
        }
        context.getSource().sendSuccess(
            () -> Component.translatable("commands.thaumcraft.wand.given", display.getDisplayName(), targets.size()),
            true
        );
        return targets.size();
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
