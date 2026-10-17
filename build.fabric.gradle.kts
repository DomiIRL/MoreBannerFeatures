plugins {
    id("dev.kikugie.loom-back-compat")
}

fun prop(key: String): String = sc.properties[key]

version = "${prop("mod.version")}+${sc.current.version}-fabric"
group = prop("mod.group")
base.archivesName = prop("mod.archives_name")

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    else -> JavaVersion.VERSION_17
}

repositories {
    exclusiveContent {
        forRepository { maven("https://maven.terraformersmc.com/") { name = "Terraformers" } }
        filter { includeGroup("com.terraformersmc") }
    }
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("mbf") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }

    runConfigs.all {
        runDirectory = rootProject.file("run")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    // Mojang mappings below 26.1, a no-op on the unobfuscated versions
    loomx.applyMojangMappings()

    // loom-back-compat turns mod* configurations into plain ones from 26.1 on
    modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric_api")}")
    // Only its API is compiled against; older Mod Menu releases pull Placeholder API from a repository we never need
    modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}") { isTransitive = false }
}

waveyCapes("modCompileOnly", sc.properties.getOrNull<String>("deps.waveycapes"), sc.properties.getOrNull<String>("deps.transition"))

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
        )
        inputs.properties(props)
        filesMatching("fabric.mod.json") { expand(props) }
        exclude("META-INF/neoforge.mods.toml", "META-INF/mods.toml", "pack.mcmeta")
    }

    withType<Jar> {
        from(rootProject.file("LICENSE")) { rename { "${it}_MoreBannerFeatures" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds the mod jar and copies it to the root build/libs/<mod version>/"
        from(loomx.modJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${prop("mod.version")}"))
    }
}

publishToModSites(loomx.modJar.flatMap { it.archiveFile }, "fabric", (sc.properties.raw("mod", "mc_releases") as List<*>).map { it.toString() }, requiredJava, sc.properties.getOrNull<String>("deps.waveycapes") != null)
