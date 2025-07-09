package dev.svrt.domiirl.mbf.mixin.ghast;

import dev.svrt.domiirl.mbf.accessor.GhastBannerable;
import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HappyGhastRenderState.class)
public class HappyGhastRenderStateMixin implements GhastBannerable {
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
