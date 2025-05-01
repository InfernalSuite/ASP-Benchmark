package com.infernalsuite.benchmark.commands.sub;

import java.io.IOException;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.exceptions.CorruptedWorldException;
import com.infernalsuite.asp.api.exceptions.NewerFormatException;
import com.infernalsuite.asp.api.loaders.SlimeSerializationAdapter;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import com.infernalsuite.benchmark.commands.ASPBenchmarkCommand;
import com.infernalsuite.benchmark.commands.exception.MessageCommandException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class DeserializeCommand {

    private final ASPBenchmarkCommand manager;
    private final SlimeSerializationAdapter serializer;

    public DeserializeCommand(ASPBenchmarkCommand manager) {
        this.manager = manager;
        this.serializer = AdvancedSlimePaperAPI.instance().getSerializer();
    }

    @Command("aspbenchmark|aspb|swmb deserialize <world> <iterations>")
    public void deserializeWorld(CommandSender source, @Argument("world") String worldName,
                                 @Argument(value = "iterations") int iterations) {
        SlimeWorld readWorld = manager.readWorldIfNull(worldName);
        SlimePropertyMap propertyMap = readWorld.getPropertyMap();

        byte[] serializedData = serializer.serializeWorld(readWorld);
        source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                Component.text("Serialized data size: " + serializedData.length / 1024 + " KB").color(NamedTextColor.GRAY)));

        // perform multiple deserialization runs for better measurements
        long totalTime = 0;
        for (int i = 0; i < iterations; i++) {
            try {
                // we're going to use nano time for better precision
                long startTime = System.nanoTime();
                SlimeWorld deserializedWorld = serializer.deserializeWorld(readWorld.getName() + "_bench",
                        serializedData,
                        manager.getFileLoader(),
                        propertyMap,
                        true);
                long endTime = System.nanoTime();

                long timeMs = (endTime - startTime) / 1_000_000; // convert to milliseconds
                totalTime += timeMs;

                source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                        Component.text("Run " + (i + 1) + ": Deserialized world in " + timeMs + "ms!").color(NamedTextColor.GRAY)));
            } catch (CorruptedWorldException | NewerFormatException | IOException e) {
                throw new MessageCommandException(ASPBenchmarkCommand.PREFIX.append(
                        Component.text("Failed to deserialize world: " + e.getMessage()).color(NamedTextColor.RED)));
            }
        }
        long avgTime = totalTime / iterations;
        source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                Component.text("Average deserialization time over " + iterations + " runs: " + avgTime + "ms").color(NamedTextColor.GREEN)));

        // report chunks
        source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                Component.text("Original world had " + readWorld.getChunkStorage().size() + " chunks").color(NamedTextColor.GRAY)));
    }
}
