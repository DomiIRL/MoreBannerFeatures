plugins {
    id("net.minecraftforge.gradle") version "7.0.40"
}

fun prop(key: String): String = sc.properties[key]

version = "${prop("mod.version")}+${sc.current.version}-forge"
group = prop("mod.group")
base.archivesName = prop("mod.archives_name")

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    else -> JavaVersion.VERSION_17
}

// Forge has no environment-split source sets. The client set only exists so Stonecutter processes
// src/client; its sources compile into the one mod jar.
val client: SourceSet by sourceSets.creating
sourceSets.main {
    java.source(client.java)
    resources.source(client.resources)
}

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
    mavenCentral()
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:${sc.current.version}-${prop("deps.forge")}"))
}

minecraft {
    runs {
        configureEach {
            workingDir = rootProject.layout.projectDirectory.dir("run/forge")
            args("--mixin.config", "mbf.mixins.json", "--mixin.config", "mbf.client.mixins.json")
        }
        register("client")
        register("server") {
            args("--nogui")
        }
    }
}

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
            "pack_format" to prop("mod.pack_format"),
        )
        inputs.properties(props)
        filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) { expand(props) }
        exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
    }

    withType<Jar> {
        from(rootProject.file("LICENSE")) { rename { "${it}_MoreBannerFeatures" } }
        manifest.attributes("MixinConfigs" to "mbf.mixins.json,mbf.client.mixins.json")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds the mod jar and copies it to the root build/libs/<mod version>/"
        from(jar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${prop("mod.version")}"))
    }
}
