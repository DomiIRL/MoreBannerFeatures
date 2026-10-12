package dev.svrt.domiirl.mbf.mixin.threads;

import dev.svrt.domiirl.mbf.recipe.UseBannerThreadsRecipe;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
//? if <1.21.5 {
/*import net.minecraft.world.item.BannerItem;
*///?}

// Before 1.21.5 the banner tooltip and its layer cap live in BannerItem
//? if >=1.21.5 {
@Mixin(BannerPatternLayers.class)
//?} else {
/*@Mixin(BannerItem.class)
*///?}
public class BannerPatternLayersMixin {

  //? if >=1.21.5 {
  @ModifyConstant(method = "addToTooltip", constant = @Constant(intValue = 6))
  private int modifyMaxLayers(int original) {
  //?} else {
  /*@ModifyConstant(method = "appendHoverTextFromBannerBlockEntityTag", constant = @Constant(intValue = 6))
  private static int modifyMaxLayers(int original) {
  *///?}
    return UseBannerThreadsRecipe.MAX_BANNER_LAYERS;
  }

}
