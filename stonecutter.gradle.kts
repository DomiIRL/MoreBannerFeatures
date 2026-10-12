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
            fun path(pkg: String) = pkg.split('.').joinToString("\\1").replaceFirst("\\1", "([./])")
            fun target(pkg: String) = pkg.split('.').joinToString("$1")
            val names = classes.joinToString("|", "(", ")")
            regex(before1_21_11) {
                replace(
                    "${path(newPackage)}\\1$names\\b", "${target(oldPackage)}$1$2",
                    "${path(oldPackage)}\\1$names\\b", "${target(newPackage)}$1$2",
                )
            }
        }
        moved("net.minecraft.client.model.animal.fox", "net.minecraft.client.model", "FoxModel")
        moved("net.minecraft.client.model.animal.ghast", "net.minecraft.client.model", "HappyGhastModel")
        moved("net.minecraft.client.model.animal.equine", "net.minecraft.client.model", "HorseModel")
        moved("net.minecraft.client.model.monster.strider", "net.minecraft.client.model", "StriderModel")
        moved("net.minecraft.client.model.player", "net.minecraft.client.model", "PlayerModel")
        moved("net.minecraft.client.renderer.rendertype", "net.minecraft.client.renderer", "RenderType")
        moved("net.minecraft.world.entity.vehicle.minecart", "net.minecraft.world.entity.vehicle",
            "AbstractMinecart", "AbstractMinecartContainer", "Minecart", "MinecartChest", "MinecartFurnace")
        moved("net.minecraft.world.entity.animal.equine", "net.minecraft.world.entity.animal.horse", "AbstractHorse")
        moved("net.minecraft.world.entity.animal.happyghast", "net.minecraft.world.entity.animal", "HappyGhast")
        moved("net.minecraft.world.entity.npc.villager", "net.minecraft.world.entity.npc", "AbstractVillager", "Villager")

        // 1.21.9 turned rendering into submitting: layers submit to a SubmitNodeCollector instead of
        // rendering into a MultiBufferSource, and players render as Avatars
        val before1_21_9 = current.parsed < "1.21.9"
        regex(before1_21_9) {
            replace("\\bSubmitNodeCollector\\b", "MultiBufferSource", "\\bMultiBufferSource\\b", "SubmitNodeCollector")
        }
        regex(before1_21_9) {
            replace(
                "\\bsubmit\\((PoseStack |Lcom/mojang/blaze3d/vertex/PoseStack;)", "render($1",
                "\\brender\\((PoseStack |Lcom/mojang/blaze3d/vertex/PoseStack;)", "submit($1",
            )
        }
        regex(before1_21_9) {
            replace("\\bAvatarRenderState\\b", "PlayerRenderState", "\\bPlayerRenderState\\b", "AvatarRenderState")
        }
        regex(before1_21_9) {
            replace("\\bAvatarRenderer\\b", "PlayerRenderer", "\\bPlayerRenderer\\b", "AvatarRenderer")
        }
        regex(before1_21_9) {
            replace(
                "net([./])minecraft\\1world\\1entity\\1Avatar\\b", "net$1minecraft$1client$1player$1AbstractClientPlayer",
                "net([./])minecraft\\1client\\1player\\1AbstractClientPlayer\\b", "net$1minecraft$1world$1entity$1Avatar",
            )
        }
        regex(before1_21_9) {
            replace("(?<![./\\w])Avatar\\b", "AbstractClientPlayer", "(?<![./\\w])AbstractClientPlayer\\b", "Avatar")
        }

        // 1.21.2 passed the ServerLevel into entity drops and split Boat into AbstractBoat and its subclasses
        val before1_21_2 = current.parsed < "1.21.2"
        regex(before1_21_2) {
            replace("\\bspawnAtLocation\\(serverLevel, ", "spawnAtLocation(", "\\bspawnAtLocation\\((?!serverLevel)", "spawnAtLocation(serverLevel, ")
        }
        regex(before1_21_2) {
            replace("\\bvoid dropEquipment\\(ServerLevel serverLevel\\)", "void dropEquipment()", "\\bvoid dropEquipment\\(\\)", "void dropEquipment(ServerLevel serverLevel)")
        }
        regex(before1_21_2) {
            replace("\\bdropEquipment\\(serverLevel\\)", "dropEquipment()", "\\bsuper\\.dropEquipment\\(\\)", "super.dropEquipment(serverLevel)")
        }
        // Replacements don't chain, so AbstractBoat gets one rule per version range instead of moved()
        regex(before1_21_11 && !before1_21_2) {
            replace("vehicle([./])boat\\1AbstractBoat\\b", "vehicle$1AbstractBoat", "vehicle([./])AbstractBoat\\b", "vehicle$1boat$1AbstractBoat")
        }
        regex(before1_21_2) {
            replace("vehicle([./])boat\\1AbstractBoat\\b", "vehicle$1Boat", "vehicle([./])Boat\\b", "vehicle$1boat$1AbstractBoat")
        }
        regex(before1_21_2) {
            replace("(?<![./\\w])AbstractBoat\\b", "Boat", "(?<![./\\w])Boat\\b", "AbstractBoat")
        }
        regex(before1_21_2) {
            replace(
                "net([./])minecraft\\1util\\1ARGB\\b", "net$1minecraft$1util$1FastColor$1ARGB32",
                "net([./])minecraft\\1util\\1FastColor\\1ARGB32\\b", "net$1minecraft$1util$1ARGB",
            )
        }
        regex(before1_21_2) {
            replace("(?<![./\\w])ARGB\\b", "ARGB32", "(?<![./\\w])ARGB32\\b", "ARGB")
        }

        // Banner patterns became a data component in 1.20.5; before that MBF reads the NBT list through its own stand-in
        val before1_20_5 = current.parsed < "1.20.5"
        regex(before1_20_5) {
            replace(
                "net([./])minecraft\\1world\\1level\\1block\\1entity\\1BannerPatternLayers\\b", "dev$1svrt$1domiirl$1mbf$1legacy$1BannerPatternLayers",
                "dev([./])svrt\\1domiirl\\1mbf\\1legacy\\1BannerPatternLayers\\b", "net$1minecraft$1world$1level$1block$1entity$1BannerPatternLayers",
            )
        }

        regex(before1_20_5) {
            replace("HolderLookup\\.Provider registryAccess\\)", "net.minecraft.core.RegistryAccess registryAccess)", "net\\.minecraft\\.core\\.RegistryAccess registryAccess\\)", "HolderLookup.Provider registryAccess)")
        }

        // 1.21 gave recipes a CraftingInput instead of the crafting grid's container itself
        val before1_21 = current.parsed < "1.21"
        regex(before1_21) {
            replace(
                "world([./])item\\1crafting\\1CraftingInput\\b", "world$1inventory$1CraftingContainer",
                "world([./])inventory\\1CraftingContainer\\b", "world$1item$1crafting$1CraftingInput",
            )
        }
        regex(before1_21) {
            replace("(?<![./\\w])CraftingInput\\b", "CraftingContainer", "(?<![./\\w])CraftingContainer\\b", "CraftingInput")
        }
        regex(before1_21) {
            replace("\\bcraftingInput\\.size\\(\\)", "craftingInput.getContainerSize()", "\\bcraftingInput\\.getContainerSize\\(\\)", "craftingInput.size()")
        }

        // Banner models only became classes in 1.21.4; before that MBF brings its own with the same API
        val before1_21_4 = current.parsed < "1.21.4"
        regex(before1_21_11 && !before1_21_4) {
            replace(
                "client([./])model\\1object\\1banner\\1(BannerModel|BannerFlagModel)\\b", "client$1model$1$2",
                "client([./])model\\1(BannerModel|BannerFlagModel)\\b", "client$1model$1object$1banner$1$2",
            )
        }
        regex(before1_21_4) {
            replace(
                "net([./])minecraft\\1client\\1model\\1object\\1banner\\1(BannerModel|BannerFlagModel)\\b", "dev$1svrt$1domiirl$1mbf$1legacy$1$2",
                "dev([./])svrt\\1domiirl\\1mbf\\1legacy\\1(BannerModel|BannerFlagModel)\\b", "net$1minecraft$1client$1model$1object$1banner$1$2",
            )
        }
    }
}
