package dev.svrt.domiirl.morebannerfeatures.mixin.player;

import dev.svrt.domiirl.morebannerfeatures.accessor.BannerRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerRenderState.class)
public class PlayerRenderStateMixin implements BannerRenderState {
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
