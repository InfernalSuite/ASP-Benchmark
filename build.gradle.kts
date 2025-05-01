import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta13"
}

group = "com.infernalsuite"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://repo.infernalsuite.com/repository/maven-snapshots/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.infernalsuite.asp:api:4.0.0-SNAPSHOT")

    implementation("org.incendo:cloud-annotations:2.0.0")
    implementation("org.incendo:cloud-paper:2.0.0-beta.10") // command framework
    implementation("com.infernalsuite.asp:file-loader:4.0.0-SNAPSHOT")
}

tasks.withType<ProcessResources> {
    // Define the encoding for resource files
    filteringCharset = "UTF-8"

    filesMatching("paper-plugin.yml") {
        // Enable property expansion for plugin.yml
        expand(project.properties) // Expands properties like 'version'
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("ASP-Benchmark")
    archiveClassifier.set("")
    archiveVersion.set(rootProject.version.toString())
}