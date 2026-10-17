// Forge up to 1.20.4 runs on SRG names, so jars are reobfuscated and mixins need a refmap.
// ForgeGradle 7 only covers the Mojang-named Forge from 1.20.6 on; ModDevGradle's legacy plugin handles these
plugins {
    id("net.neoforged.moddev.legacyforge") version "2.0.148"
    id("neoforge-mutex")
}

fun prop(key: String): String = sc.properties[key]

version = "${prop("mod.version")}+${sc.current.version}-forge"
group = prop("mod.group")
base.archivesName = prop("mod.archives_name")

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    else -> JavaVersion.VERSION_1_8
}

// Forge has no environment-split source sets. The client set only exists so Stonecutter processes
// src/client; its sources compile into the one mod jar.
val client: SourceSet by sourceSets.creating
sourceSets.main {
    java.source(client.java)
    resources.source(client.resources)
}

legacyForge {
    version = "${sc.current.version}-${prop("deps.forge")}"

    mods {
        register("mbf") {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            client()
            gameDirectory = rootProject.file("run/forge")
        }
        register("server") {
            server()
            gameDirectory = rootProject.file("run/forge")
            programArgument("--nogui")
        }
    }
}

mixin {
    add(sourceSets.main.get(), "mbf.refmap.json")
    config("mbf.mixins.json")
    config("mbf.client.mixins.json")
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
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
        legacyData { sc.current.parsed < it }
        val props = mapOf(
            "version" to prop("mod.version"),
            "minecraft" to prop("mod.mc_compat"),
            "pack_format" to "\"pack_format\": ${prop("mod.pack_format")}",
        )
        inputs.properties(props)
        filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) { expand(props) }
        // Both configs share the one refmap the annotation processor writes for the merged source set
        filesMatching(listOf("mbf.mixins.json", "mbf.client.mixins.json")) {
            filter { line -> if (line.trimStart().startsWith("\"package\"")) "$line\n  \"refmap\": \"mbf.refmap.json\"," else line }
        }
        exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    withType<Jar> {
        from(rootProject.file("LICENSE")) { rename { "${it}_MoreBannerFeatures" } }
        manifest.attributes("MixinConfigs" to "mbf.mixins.json,mbf.client.mixins.json")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds the reobfuscated mod jar and copies it to the root build/libs/<mod version>/"
        from(named<Jar>("reobfJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${prop("mod.version")}"))
    }
}

publishToModSites(tasks.named<Jar>("reobfJar").flatMap { it.archiveFile }, "forge", listOf(sc.current.version), requiredJava, sc.properties.getOrNull<String>("deps.waveycapes") != null)
