package com.infernalsuite.benchmark.service;

public interface Service {

    void load();
    default void unload() {}
}
