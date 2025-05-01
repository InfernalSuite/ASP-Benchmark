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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class LoadCommand {

    private final ASPBenchmarkCommand manager;

    public LoadCommand(ASPBenchmarkCommand manager) {
        this.manager = manager;
    }

    @Command("aspbenchmark|aspb|swmb load <world> <iterations>")
    public void loadWorld(CommandSender source, @Argument("world") String worldName,
                          @Argument(value = "iterations") int iterations) {
        if (Bukkit.getWorld(worldName) != null) {
            throw new MessageCommandException(ASPBenchmarkCommand.PREFIX.append(Component.text("World is already loaded!").color(NamedTextColor.RED)));
        }
        SlimeLoader loader = manager.getFileLoader();
        try {
            SlimeWorld readWorld = AdvancedSlimePaperAPI.instance().readWorld(loader, worldName, true, new SlimePropertyMap());
            long totalTime = 0;
            for (int i = 0; i < iterations; i++) {
                long startTime = System.nanoTime();

                SlimeWorldInstance loadedWorld = AdvancedSlimePaperAPI.instance().loadWorld(readWorld, true);

                long endTime = System.nanoTime();
                long timeMs = (endTime - startTime) / 1_000_000; // convert to milliseconds

                totalTime += timeMs;

                source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                        Component.text("Run " + (i + 1) + ": Loaded world in " + timeMs + "ms")
                                 .color(NamedTextColor.GRAY)));

                Bukkit.unloadWorld(loadedWorld.getBukkitWorld(), false);
                source.sendMessage(ASPBenchmarkCommand.PREFIX.append(Component.text("Unloaded world on iteration " + (i + 1)).color(NamedTextColor.GRAY)));
            }

            long avgTime = totalTime / iterations;
            source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                    Component.text("Average loading time over " + iterations + " runs: " + avgTime + "ms").color(NamedTextColor.GREEN)));
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException e) {
            throw new MessageCommandException(ASPBenchmarkCommand.PREFIX.append(Component.text("Failed to load world: " + e.getMessage()).color(NamedTextColor.RED)));
        }
    }
}
