package thaumcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.network.BeamPulsePayload;
import thaumcraft.platform.ModNetwork;

@EventBusSubscriber(modid = Thaumcraft.MODID)
public final class ThaumcraftCommand {
    private static final int DEFAULT_BEAM_COLOR = 0xFF00FF;

    private ThaumcraftCommand() {
    }

    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
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
        );
    }

    private static int beam(CommandContext<CommandSourceStack> context, int color) throws CommandSyntaxException {
        Entity source = context.getSource().getEntityOrException();
        Entity target = EntityArgument.getEntity(context, "target");
        ModNetwork.sendToTrackingAndSelf(source, new BeamPulsePayload(source.getId(), target.getId(), color));
        return 1;
    }
}
