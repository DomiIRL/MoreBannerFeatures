import org.gradle.language.jvm.tasks.ProcessResources

// The data folder is written for the newest Minecraft; this rewrites it for what an older node reads.
// `before` compares the node's Minecraft version, e.g. before("1.21").
fun ProcessResources.legacyData(before: (String) -> Boolean) {
    // A shaped recipe can only put data on its result from 1.20.5, and the threads need their layer count.
    // Older nodes craft them through BannerThreadRecipe instead, whose serializer only exists there.
    if (before("1.20.5")) {
        exclude("data/mbf/recipe/banner_thread_*.json")
    } else {
        exclude("data/mbf/recipe/banner_thread.json")
    }
    if (before("1.21.2")) {
        // Ingredients are plain ids from 1.21.2, objects before
        filesMatching("data/*/recipe/*.json") {
            filter { line -> line.replace(Regex("""^(\s*"\w": )"([^"]+)""""), """$1{"item": "$2"}""") }
        }
    }
    if (before("1.21")) {
        // 1.21 renamed the plural datapack folders
        eachFile { path = path.replace("/tags/item/", "/tags/items/").replace("/recipe/", "/recipes/") }
        includeEmptyDirs = false
    }
}
