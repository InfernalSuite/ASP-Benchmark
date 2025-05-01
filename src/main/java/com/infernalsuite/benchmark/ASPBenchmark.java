package com.infernalsuite.benchmark;

import org.bukkit.plugin.java.JavaPlugin;

public class ASPBenchmark extends JavaPlugin {

    private final ASPBenchmarkLoader loader = new ASPBenchmarkLoader(this);

    @Override
    public void onEnable() {
        loader.load();
    }

    @Override
    public void onDisable() {
        loader.unload();
    }
}
