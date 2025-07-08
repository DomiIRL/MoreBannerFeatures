package dev.svrt.domiirl.mbf.mixin.threads;

import dev.svrt.domiirl.mbf.recipe.UseBannerThreadsRecipe;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BannerPatternLayers.class)
public class BannerPatternLayersMixin {

  @ModifyConstant(method = "addToTooltip", constant = @Constant(intValue = 6))
  private int modifyMaxLayers(int original) {
    return UseBannerThreadsRecipe.MAX_BANNER_LAYERS;
  }

}
