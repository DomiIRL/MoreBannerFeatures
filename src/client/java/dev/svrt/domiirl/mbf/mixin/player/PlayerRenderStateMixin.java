package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerRenderState.class)
public class PlayerRenderStateMixin implements Bannerable {
  public ItemStack bannerItem = ItemStack.EMPTY;

  @Override
  public @NotNull ItemStack moreBannerFeatures$getBannerItem() {
    return bannerItem;
  }

  @Override
  public void moreBannerFeatures$setBannerItem(@NotNull ItemStack stack) {
    this.bannerItem = stack;
  }
}
