import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.kotlin.dsl.maven

// Compiles against WaveyCapes on the nodes that set both versions. TRansition comes separately because
// WaveyCapes only ships it nested in its jar, which no compile classpath looks into.
fun Project.waveyCapes(configuration: String, waveyCapes: String?, transition: String?) {
    if (waveyCapes == null || transition == null) {
        return
    }

    repositories.exclusiveContent {
        forRepository { repositories.maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
        filter { includeGroup("maven.modrinth") }
    }
    repositories.exclusiveContent {
        forRepository { repositories.maven("https://maven.tr7zw.dev/repository/maven-public/") { name = "tr7zw" } }
        filter { includeGroup("dev.tr7zw") }
    }

    dependencies.add(configuration, "maven.modrinth:wavey-capes:$waveyCapes")
    (dependencies.add(configuration, "dev.tr7zw:TRansition:$transition") as ModuleDependency).isTransitive = false
}
