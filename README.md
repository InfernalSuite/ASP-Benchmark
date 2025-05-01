# ASP Benchmark

A Minecraft plugin for benchmarking Advanced Slime API (ASP) performance.

## Overview

ASP Benchmark is a tool designed to test and measure the performance of the Advanced Slime Paper API for world operations. It provides commands to:

1. Serialize Slime worlds and measure performance
2. Deserialize Slime worlds and measure performance
3. Load worlds and measure performance

## Commands

### Main Command

* `/aspbenchmark` (aliases: `/aspb`, `/swmb`) - Main command that displays help information

### Subcommands

* `/aspbenchmark serialize <world> <iterations>` - Benchmark world serialization
    * `<world>` - Name of the world to serialize
    * `<iterations>` - Number of serialization runs to perform for averaging

* `/aspbenchmark deserialize <world> <iterations>` - Benchmark world deserialization
    * `<world>` - Name of the world to deserialize
    * `<iterations>` - Number of deserialization runs to perform for averaging

* `/aspbenchmark load <world> <iterations>` - Benchmark world loading
    * `<world>` - Name of the world to load
    * `<iterations>` - Number of load runs to perform for averaging

* `/aspbenchmark help` - Display available commands

## Metrics Measured

* Serialization time (milliseconds)
* Deserialization time (milliseconds)
* Loading time (milliseconds)
* Serialized data size (KB)
* Number of chunks in the world

## Requirements

- AdvancedSlimePaper Minecraft server

## Installation

1. Download the plugin JAR from releases
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Use the commands to benchmark ASP operations

## Build from Source

This project uses Gradle for building:

```bash
./gradlew shadowJar