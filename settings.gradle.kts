pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.minecraftforge.net/") { name = "MinecraftForge" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
    // Picks the remapping loom below 26.1 and the unobfuscated one from 26.1 on
    id("dev.kikugie.loom-back-compat") version "0.4.2"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// ModDevGradle's legacy plugin covers Forge 1.17 to 1.20.1; Forge 1.20.2-1.20.4 has no Gradle 9 toolchain, NeoForge covers those
val legacyForge = setOf("1.20.1", "1.19.2", "1.18.2")

stonecutter {
    create(rootProject) {
        // One node per Minecraft version and loader, at versions/<version>-<loader>, each built by build.<loader>.gradle.kts
        fun mc(version: String, vararg loaders: String) {
            for (loader in loaders) {
                // Forge before 1.20.5 runs on SRG names and needs the reobfuscating legacy build
                val script = if (loader == "forge" && version in legacyForge) "legacyforge" else loader
                version("$version-$loader", version).buildscript("build.$script.gradle.kts")
            }
        }

        mc("26.3", "fabric", "neoforge", "forge")
        mc("26.2", "fabric", "neoforge", "forge")
        mc("26.1.2", "fabric", "neoforge", "forge")
        mc("1.21.11", "fabric", "neoforge", "forge")
        mc("1.21.10", "fabric", "neoforge", "forge")
        mc("1.21.8", "fabric", "neoforge", "forge")
        mc("1.21.5", "fabric", "neoforge", "forge")
        mc("1.21.4", "fabric", "neoforge", "forge")
        mc("1.21.1", "fabric", "neoforge", "forge")
        mc("1.20.6", "fabric", "neoforge", "forge")
        mc("1.20.4", "fabric", "neoforge")
        // NeoForge 1.20.1 is a Forge 47.1 fork and loads the Forge jar
        mc("1.20.1", "fabric", "forge")
        mc("1.19.2", "fabric", "forge")
        vcsVersion = "26.3-fabric"
    }
}

rootProject.name = "MoreBannerFeatures"
