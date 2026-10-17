import me.modmuss50.mpp.ModPublishExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure

// Uploads one node's jar to Modrinth and CurseForge through `publishMods`. Without the tokens in the
// environment it only does a dry run, so the task can be checked locally.
fun Project.publishToModSites(jar: Provider<RegularFile>, loader: String, minecraftVersions: List<String>, java: JavaVersion, waveyCapes: Boolean) {
    pluginManager.apply("me.modmuss50.mod-publish-plugin")

    val modVersion = version.toString().substringBefore('+')
    val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
    val curseforgeToken = providers.environmentVariable("CURSEFORGE_TOKEN")
    val loaderName = loader.replaceFirstChar { it.uppercase() }.replace("forge", "Forge")

    extensions.configure<ModPublishExtension> {
        file.set(jar)
        version.set(project.version.toString())
        displayName.set("$modVersion for ${minecraftVersions.last()} $loaderName")
        changelog.set(providers.environmentVariable("RELEASE_CHANGELOG").orElse(""))
        type.set(
            when {
                "alpha" in modVersion -> ALPHA
                "beta" in modVersion -> BETA
                else -> STABLE
            }
        )
        modLoaders.add(loader)
        dryRun.set(modrinthToken.orNull == null || curseforgeToken.orNull == null)

        modrinth {
            accessToken.set(modrinthToken)
            projectId.set("FOaR4sIZ")
            this.minecraftVersions.addAll(minecraftVersions)
            if (loader == "fabric") {
                requires("fabric-api")
                optional("modmenu", "trinkets", "trinkets-canary")
            }
            optional("accessories")
            if (waveyCapes) {
                optional("wavey-capes")
            }
        }

        curseforge {
            accessToken.set(curseforgeToken)
            projectId.set("515000")
            this.minecraftVersions.addAll(minecraftVersions)
            javaVersions.add(java)
            client.set(true)
            server.set(true)
            if (loader == "fabric") {
                requires("fabric-api")
                optional("modmenu", "trinkets")
            }
            optional("accessories")
            if (waveyCapes) {
                optional("waveycapes")
            }
        }
    }
}
