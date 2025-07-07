package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.accessor.BannerRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerRenderState.class)
public class PlayerRenderStateMixin implements BannerRenderState {
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
