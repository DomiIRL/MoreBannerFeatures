plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3-fabric"

stonecutter parameters {
    val (_, loader) = current.project.split('-', limit = 2)

    properties {
        tags(current.version, loader)
    }

    constants {
        match(loader, "fabric", "neoforge", "forge")
    }

    // Classes Mojang only renamed or moved. The sources use the newest name; older nodes get theirs here
    // instead of an //? if at every use. Each spec covers import paths and mixin descriptors alike.
    replacements {
        val before26 = current.parsed < "26.1"
        regex(before26) {
            replace(
                "client([./])resources\\1model\\1sprite\\1SpriteGetter\\b", "client$1resources$1model$1MaterialSet",
                "client([./])resources\\1model\\1MaterialSet\\b", "client$1resources$1model$1sprite$1SpriteGetter",
            )
        }
        regex(before26) {
            replace("(?<![./\\w])SpriteGetter\\b", "MaterialSet", "(?<![./\\w])MaterialSet\\b", "SpriteGetter")
        }
        regex(before26) {
            replace(
                "client([./])resources\\1model\\1sprite\\1SpriteId\\b", "client$1resources$1model$1Material",
                "client([./])resources\\1model\\1Material\\b", "client$1resources$1model$1sprite$1SpriteId",
            )
        }
        regex(before26) {
            replace("(?<![./\\w])SpriteId\\b", "Material", "(?<![./\\w])Material\\b", "SpriteId")
        }
        regex(before26) {
            replace("(?<![./\\w])GuiGraphicsExtractor\\b", "GuiGraphics", "(?<![./\\w])GuiGraphics\\b", "GuiGraphicsExtractor")
        }
        regex(before26) {
            replace("gui([./])GuiGraphicsExtractor\\b", "gui$1GuiGraphics", "gui([./])GuiGraphics\\b", "gui$1GuiGraphicsExtractor")
        }
        // EntityRendererProvider.Context
        regex(before26) {
            replace("\\.getSprites\\(\\)", ".getMaterials()", "\\.getMaterials\\(\\)", ".getSprites()")
        }
        regex(before26) {
            replace(
                "renderer([./])state\\1level\\1CameraRenderState\\b", "renderer$1state$1CameraRenderState",
                "renderer([./])state\\1CameraRenderState\\b", "renderer$1state$1level$1CameraRenderState",
            )
        }
    }
}
