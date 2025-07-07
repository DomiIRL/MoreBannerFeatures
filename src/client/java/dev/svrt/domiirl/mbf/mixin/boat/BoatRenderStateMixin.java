package dev.svrt.domiirl.mbf.mixin.boat;

import dev.svrt.domiirl.mbf.accessor.BannerRenderState;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatRenderState.class)
public class BoatRenderStateMixin implements BannerRenderState {
  public ItemStack bannerItem = ItemStack.EMPTY;

  @Override
  public @NotNull ItemStack getBannerItem() {
    return bannerItem;
  }

  @Override
  public void setBannerItem(ItemStack stack) {
    this.bannerItem = stack;
  }
}
