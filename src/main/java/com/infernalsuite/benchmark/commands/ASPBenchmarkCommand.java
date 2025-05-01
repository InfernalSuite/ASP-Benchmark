package com.infernalsuite.benchmark.commands;

import java.util.logging.Logger;

import com.infernalsuite.benchmark.ASPBenchmark;
import com.infernalsuite.benchmark.ASPBenchmarkLoader;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.incendo.cloud.paper.PaperCommandManager;

public class ASPBenchmarkCommand {

    public static final TextComponent PREFIX = LegacyComponentSerializer.legacySection().deserialize("" +
            "§9§lASPBenchmark §7§l>> §r");
    private final ASPBenchmark plugin;

    public ASPBenchmarkCommand(ASPBenchmark plugin) {
        this.plugin = plugin;

        LegacyPaperCommandManager<CommandSender> commandManager = LegacyPaperCommandManager.createNative(
                plugin,
                ExecutionCoordinator.coordinatorFor(ExecutionCoordinator.nonSchedulingExecutor())
        );

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier();
        } else {
            ASPBenchmarkLoader.LOGGER.warning("Brigadier is not supported on this server version!");
        }

        AnnotationParser<CommandSourceStack> annotationParser = new AnnotationParser<>(commandManager, CommandSourceStack.class);
        // parse
    }

    @Command("aspbenchmark|aspb")
    public void onCommand(CommandSender source) {
        source.sendMessage(PREFIX.append(Component.text("This is the main command for ASPBenchmark!").color(NamedTextColor.GRAY)
                .append(Component.text("/aspbenchmark help").color(NamedTextColor.YELLOW))
                .append(Component.text(" to see all available commands!")).color(NamedTextColor.GRAY)));
    }
}
