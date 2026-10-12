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

        val before1_21_11 = current.parsed < "1.21.11"
        regex(before1_21_11) {
            replace("\\bIdentifier\\b", "ResourceLocation", "\\bResourceLocation\\b", "Identifier")
        }
        regex(before1_21_11) {
            replace("\\bRenderTypes::", "RenderType::", "\\bRenderType::", "RenderTypes::")
        }
        // Classes 1.21.11 moved into a subpackage, newest package first
        fun moved(newPackage: String, oldPackage: String, vararg classes: String) {
            fun path(pkg: String) = pkg.split('.').joinToString("\\1", prefix = "\\b").replaceFirst("\\1", "([./])")
            fun target(pkg: String) = pkg.split('.').joinToString("$1")
            val names = classes.joinToString("|", "(", ")")
            regex(before1_21_11) {
                replace(
                    "${path(newPackage)}\\1$names\\b", "${target(oldPackage)}$1$2",
                    "${path(oldPackage)}\\1$names\\b", "${target(newPackage)}$1$2",
                )
            }
        }
        moved("net.minecraft.client.model.object.banner", "net.minecraft.client.model", "BannerFlagModel", "BannerModel")
        moved("net.minecraft.client.model.animal.fox", "net.minecraft.client.model", "FoxModel")
        moved("net.minecraft.client.model.animal.ghast", "net.minecraft.client.model", "HappyGhastModel")
        moved("net.minecraft.client.model.animal.equine", "net.minecraft.client.model", "HorseModel")
        moved("net.minecraft.client.model.monster.strider", "net.minecraft.client.model", "StriderModel")
        moved("net.minecraft.client.model.player", "net.minecraft.client.model", "PlayerModel")
        moved("net.minecraft.client.renderer.rendertype", "net.minecraft.client.renderer", "RenderType")
        moved("net.minecraft.world.entity.vehicle.boat", "net.minecraft.world.entity.vehicle", "AbstractBoat")
        moved("net.minecraft.world.entity.vehicle.minecart", "net.minecraft.world.entity.vehicle",
            "AbstractMinecart", "AbstractMinecartContainer", "Minecart", "MinecartChest", "MinecartFurnace")
        moved("net.minecraft.world.entity.animal.equine", "net.minecraft.world.entity.animal.horse", "AbstractHorse")
        moved("net.minecraft.world.entity.animal.happyghast", "net.minecraft.world.entity.animal", "HappyGhast")
        moved("net.minecraft.world.entity.npc.villager", "net.minecraft.world.entity.npc", "AbstractVillager", "Villager")
    }
}
