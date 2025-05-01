package com.infernalsuite.benchmark;

import java.util.logging.Logger;

import com.infernalsuite.benchmark.service.Service;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.incendo.cloud.paper.PaperCommandManager;

public class ASPBenchmarkLoader implements Service {

    private final ASPBenchmark plugin;
    public static Logger LOGGER;

    public ASPBenchmarkLoader(ASPBenchmark plugin) {
        this.plugin = plugin;
        LOGGER = plugin.getLogger();
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {
        Service.super.unload();
    }
}
