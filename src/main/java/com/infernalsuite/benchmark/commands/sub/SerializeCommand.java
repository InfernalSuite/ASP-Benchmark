package com.infernalsuite.benchmark.commands.sub;

import com.infernalsuite.asp.api.AdvancedSlimePaperAPI;
import com.infernalsuite.asp.api.loaders.SlimeSerializationAdapter;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.SlimeWorldInstance;
import com.infernalsuite.benchmark.ASPBenchmarkLoader;
import com.infernalsuite.benchmark.commands.ASPBenchmarkCommand;
import com.infernalsuite.benchmark.commands.exception.MessageCommandException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class SerializeCommand {

    private final SlimeSerializationAdapter serializer;

    public SerializeCommand() {
        this.serializer = AdvancedSlimePaperAPI.instance().getSerializer();
    }

    @Command("aspbenchmark|aspb serialize <world>")
    public void serializeWorld(CommandSender source, @Argument("world") String world) {
        SlimeWorld slimeWorld = AdvancedSlimePaperAPI.instance().getLoadedWorld(world).getSerializableCopy();
        if (slimeWorld == null) {
            throw new MessageCommandException(ASPBenchmarkCommand.PREFIX.append(Component.text("World is not loaded!").color(NamedTextColor.RED)));
        }
        long startTime = System.currentTimeMillis();
        byte[] serializedData = serializer.serializeWorld(slimeWorld);
        long endTime = System.currentTimeMillis();

        ASPBenchmarkLoader.LOGGER.info("Serialized world " + world + " in " + (endTime - startTime) + "ms!");
        source.sendMessage(ASPBenchmarkCommand.PREFIX.append(Component.text("Serialized world " + world + " in " + (endTime - startTime) + "ms!").color(NamedTextColor.GRAY)));
    }
}
