package com.infernalsuite.benchmark.commands;

import com.infernalsuite.benchmark.ASPBenchmark;
import com.infernalsuite.benchmark.ASPBenchmarkLoader;
import com.infernalsuite.benchmark.commands.exception.MessageCommandException;
import com.infernalsuite.benchmark.commands.sub.SerializeCommand;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.exception.ArgumentParseException;
import org.incendo.cloud.exception.CommandExecutionException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.exception.handling.ExceptionHandler;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;

public class ASPBenchmarkCommand {

    public static final TextComponent PREFIX = LegacyComponentSerializer.legacySection().deserialize("" +
            "§9§lASPBenchmark §7§l>> §r");

    public ASPBenchmarkCommand(ASPBenchmark plugin) {

        LegacyPaperCommandManager<CommandSender> commandManager = LegacyPaperCommandManager.createNative(
                plugin,
                ExecutionCoordinator.coordinatorFor(ExecutionCoordinator.nonSchedulingExecutor())
        );

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier();
        } else {
            ASPBenchmarkLoader.LOGGER.warning("Brigadier is not supported on this server version!");
        }

        commandManager.exceptionController().registerHandler(TypeToken.get(CommandExecutionException.class), ExceptionHandler.unwrappingHandler());
        commandManager.exceptionController().registerHandler(TypeToken.get(ArgumentParseException.class), context -> {
           Throwable cause = context.exception().getCause();
           if (cause instanceof MessageCommandException message) {
               context.context().sender().sendMessage(message.getComponent());
           } else {
               String message = cause.getMessage();
               if (message == null) {
                     message = "An unknown error occurred!";
               }
               context.context().sender().sendMessage(PREFIX.append(Component.text(message).color(NamedTextColor.RED)));
           }
        });
        commandManager.exceptionController().registerHandler(TypeToken.get(InvalidSyntaxException.class), context -> {
            InvalidSyntaxException exception = context.exception();

            if (exception.currentChain().size() == 1) {
                context.context().sender().sendMessage(PREFIX.append(
                        Component.text("Unknown subcommand. To check out help page, type ").color(NamedTextColor.RED)
                                .append(Component.text("/aspbenchmark help")).color(NamedTextColor.GRAY)));
            } else {
                context.context().sender().sendMessage(PREFIX.append(
                        Component.text("Command usage: ").color(NamedTextColor.RED)
                                .append(Component.text("/" + exception.correctSyntax()).color(NamedTextColor.GRAY))
                ));
            }
        });
        commandManager.exceptionController().registerHandler(TypeToken.get(NoPermissionException.class), context -> {
            context.context().sender().sendMessage(PREFIX.append(
                    Component.text("You do not have permission to execute this command!").color(NamedTextColor.RED)
            ));
        });
        commandManager.exceptionController().registerHandler(TypeToken.get(MessageCommandException.class), context -> {
            context.context().sender().sendMessage(context.exception().getComponent());
        });

        AnnotationParser<CommandSender> parser = new AnnotationParser<>(commandManager, CommandSender.class);
        parser.parse(this,
                new SerializeCommand());
    }

    @Command("aspbenchmark|aspb")
    public void onCommand(CommandSender source) {
        source.sendMessage(PREFIX.append(Component.text("This is the main command for ASPBenchmark! ").color(NamedTextColor.GRAY)
                .append(Component.text("/aspbenchmark help").color(NamedTextColor.YELLOW))
                .append(Component.text(" to see all available commands!")).color(NamedTextColor.GRAY)));
    }
}
