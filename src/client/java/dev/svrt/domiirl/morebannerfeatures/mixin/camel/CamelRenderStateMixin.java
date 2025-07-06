package dev.svrt.domiirl.morebannerfeatures.mixin.camel;

import dev.svrt.domiirl.morebannerfeatures.accessor.BannerRenderState;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CamelRenderState.class)
public class CamelRenderStateMixin implements BannerRenderState {
  public ItemStack bannerItem = ItemStack.EMPTY;

  @Override
  public ItemStack getBannerItem() {
    return bannerItem;
  }

  @Override
  public void setBannerItem(ItemStack stack) {
    this.bannerItem = stack;
  }
}
