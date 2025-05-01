package com.infernalsuite.benchmark.commands.sub;

import java.io.IOException;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.exceptions.CorruptedWorldException;
import com.infernalsuite.asp.api.exceptions.NewerFormatException;
import com.infernalsuite.asp.api.exceptions.UnknownWorldException;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.SlimeWorldInstance;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import com.infernalsuite.benchmark.commands.ASPBenchmarkCommand;
import com.infernalsuite.benchmark.commands.exception.MessageCommandException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class LoadCommand {

    private final ASPBenchmarkCommand manager;

    public LoadCommand(ASPBenchmarkCommand manager) {
        this.manager = manager;
    }

    @Command("aspbenchmark|aspb|swmb load <world>")
    public void loadWorld(CommandSender source, @Argument("world") String worldName) {
        if (AdvancedSlimePaperAPI.instance().getLoadedWorld(worldName) != null) {
            throw new MessageCommandException(ASPBenchmarkCommand.PREFIX.append(Component.text("World is already loaded!").color(NamedTextColor.RED)));
        }
        SlimeLoader loader = manager.getFileLoader();
        try {
            long startTime = System.nanoTime();

            SlimeWorld slimeWorld = AdvancedSlimePaperAPI.instance().readWorld(loader, worldName, false, new SlimePropertyMap()); // deserialize in memory
            SlimeWorldInstance realWorld = AdvancedSlimePaperAPI.instance().loadWorld(slimeWorld, true);

            long endTime = System.nanoTime();
            long timeMs = (endTime - startTime) / 1_000_000; // convert to milliseconds

            source.sendMessage(ASPBenchmarkCommand.PREFIX.append(Component.text("Loaded world " + worldName + " in " + timeMs + "ms!").color(NamedTextColor.GRAY)));
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException e) {
            throw new MessageCommandException(ASPBenchmarkCommand.PREFIX.append(Component.text("Failed to load world: " + e.getMessage()).color(NamedTextColor.RED)));
        }
    }
}
