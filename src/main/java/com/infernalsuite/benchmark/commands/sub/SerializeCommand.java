package com.infernalsuite.benchmark.commands.sub;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.loaders.SlimeSerializationAdapter;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.benchmark.commands.ASPBenchmarkCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class SerializeCommand {

    private final ASPBenchmarkCommand manager;
    private final SlimeSerializationAdapter serializer;

    public SerializeCommand(ASPBenchmarkCommand manager) {
        this.manager = manager;
        this.serializer = AdvancedSlimePaperAPI.instance().getSerializer();
    }

    @Command("aspbenchmark|aspb|swmb serialize <world> <iterations>")
    public void serializeWorld(CommandSender source, @Argument("world") String world,
                               @Argument(value = "iterations") int iterations) {
        SlimeWorld slimeWorld = manager.readWorldIfNull(world);

        long totalTime = 0;
        byte[] serializedData = null;

        for (int i = 0; i < iterations; i++) {
            // we're going to use nano time for better precision
            long startTime = System.nanoTime();
            serializedData = serializer.serializeWorld(slimeWorld);
            long endTime = System.nanoTime();

            long timeMs = (endTime - startTime) / 1_000_000; // convert to milliseconds
            totalTime += timeMs;

            source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                            Component.text("Run " + (i + 1) + ": Serialized world in " + timeMs + "ms")
                                    .color(NamedTextColor.GRAY)));
        }

        long avgTime = totalTime / iterations;
        source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                Component.text("Average serialization time over " + iterations + " runs: " + avgTime + "ms").color(NamedTextColor.GREEN)));
        source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                Component.text("Serialized data size: " + (serializedData.length / 1024) + " KB").color(NamedTextColor.GRAY)));
        source.sendMessage(ASPBenchmarkCommand.PREFIX.append(
                Component.text("Original world had " + slimeWorld.getChunkStorage().size() + " chunks").color(NamedTextColor.GRAY)));
    }
}
