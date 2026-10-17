plugins {
    id("net.neoforged.moddev") version "2.0.148"
    id("neoforge-mutex")
}

fun prop(key: String): String = sc.properties[key]

version = "${prop("mod.version")}+${sc.current.version}-neoforge"
group = prop("mod.group")
base.archivesName = prop("mod.archives_name")

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    else -> JavaVersion.VERSION_17
}

// NeoForge has no environment-split source sets. The client set only exists so Stonecutter processes
// src/client; its sources compile into the one mod jar.
val client: SourceSet by sourceSets.creating
sourceSets.main {
    java.source(client.java)
    resources.source(client.resources)
}

neoForge {
    version = prop("deps.neoforge")

    mods {
        register("mbf") {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            client()
            gameDirectory = rootProject.file("run/neoforge")
        }
        register("server") {
            server()
            gameDirectory = rootProject.file("run/neoforge")
        }
    }
}

waveyCapes("compileOnly", sc.properties.getOrNull<String>("deps.waveycapes"), sc.properties.getOrNull<String>("deps.transition"))

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava
    toolchain.languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
}

tasks {
    processResources {
        val props = mapOf(
            "version" to prop("mod.version"),
            "minecraft" to prop("mod.mc_compat"),
            // FML 12 (26.3) rejects logoFile, every older FML only knows logoFile
            "icon_key" to if (sc.current.parsed >= "26.3") "iconFile" else "logoFile",
        )
        inputs.properties(props)
        // NeoForge reads META-INF/mods.toml until 20.5
        val metadataName = if (sc.current.parsed >= "1.20.5") "neoforge.mods.toml" else "mods.toml"
        inputs.property("metadataName", metadataName)
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(props)
            name = metadataName
        }
        exclude("fabric.mod.json", "META-INF/mods.toml", "pack.mcmeta")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    withType<Jar> {
        from(rootProject.file("LICENSE")) { rename { "${it}_MoreBannerFeatures" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds the mod jar and copies it to the root build/libs/<mod version>/"
        from(jar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${prop("mod.version")}"))
    }
}
