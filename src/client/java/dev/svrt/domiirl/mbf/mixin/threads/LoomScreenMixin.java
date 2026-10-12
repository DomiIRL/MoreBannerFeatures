package dev.svrt.domiirl.mbf.mixin.threads;

import dev.svrt.domiirl.mbf.utils.BannerItems;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoomScreen.class)
public abstract class LoomScreenMixin extends AbstractContainerScreen<LoomMenu> {


  @Shadow private boolean hasMaxPatterns;

  public LoomScreenMixin(LoomMenu abstractContainerMenu, Inventory inventory, Component component) {
    super(abstractContainerMenu, inventory, component);
  }

  @Inject(method = "containerChanged", at = @At(value = "TAIL"))
  private void containerChanged(CallbackInfo ci) {
    ItemStack bannerItem = this.menu.getBannerSlot().getItem();
    BannerPatternLayers bannerPatternLayers = BannerItems.patterns(bannerItem);
    this.hasMaxPatterns = bannerPatternLayers.layers().size() >= BannerItems.maxLayers(bannerItem, BannerItems.VANILLA_MAX_LAYERS);
  }

  @ModifyConstant(method = "containerChanged", constant = @Constant(intValue = 6, ordinal = 0))
  public int getLimit(int constant) {
    ItemStack bannerItem = this.menu.getBannerSlot().getItem();
    return BannerItems.maxLayers(bannerItem, BannerItems.VANILLA_MAX_LAYERS);
  }
}
