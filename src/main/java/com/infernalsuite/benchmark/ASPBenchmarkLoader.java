package com.infernalsuite.benchmark;

import java.util.logging.Logger;

import com.infernalsuite.benchmark.commands.ASPBenchmarkCommand;
import com.infernalsuite.benchmark.service.Service;

public class ASPBenchmarkLoader implements Service {

    private final ASPBenchmark plugin;
    public static Logger LOGGER;

    public ASPBenchmarkLoader(ASPBenchmark plugin) {
        this.plugin = plugin;
        LOGGER = plugin.getLogger();
    }

    @Override
    public void load() {
        LOGGER.info("Loading ASPBenchmark...");
        new ASPBenchmarkCommand(plugin);
    }

    @Override
    public void unload() {
        LOGGER.info("Unloading ASPBenchmark...");
    }
}
