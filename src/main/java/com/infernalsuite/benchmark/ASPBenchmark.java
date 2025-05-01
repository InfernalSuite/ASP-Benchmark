package com.infernalsuite.benchmark;

import java.util.logging.Logger;

import com.infernalsuite.benchmark.commands.ASPBenchmarkCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ASPBenchmark extends JavaPlugin {

    public static Logger LOGGER;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        new ASPBenchmarkCommand(this);
    }

    @Override
    public void onDisable() {
    }
}
