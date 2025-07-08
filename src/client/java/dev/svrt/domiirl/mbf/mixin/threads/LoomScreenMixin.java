package dev.svrt.domiirl.mbf.mixin.threads;

import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.core.component.DataComponents;
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
    BannerPatternLayers bannerPatternLayers = bannerItem.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
    this.hasMaxPatterns = bannerPatternLayers.layers().size() >= bannerItem.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
  }

  @ModifyConstant(method = "containerChanged", constant = @Constant(intValue = 6, ordinal = 0))
  public int getLimit(int constant) {
    ItemStack bannerItem = this.menu.getBannerSlot().getItem();
    return bannerItem.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
  }


}
