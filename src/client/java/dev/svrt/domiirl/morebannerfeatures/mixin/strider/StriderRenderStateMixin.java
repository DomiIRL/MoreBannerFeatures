package dev.svrt.domiirl.morebannerfeatures.mixin.strider;

import dev.svrt.domiirl.morebannerfeatures.accessor.BannerRenderState;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StriderRenderState.class)
public class StriderRenderStateMixin implements BannerRenderState {
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
