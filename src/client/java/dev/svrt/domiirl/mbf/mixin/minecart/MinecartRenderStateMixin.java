package dev.svrt.domiirl.mbf.mixin.minecart;

import dev.svrt.domiirl.mbf.accessor.MinecartBannerable;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecartRenderState.class)
public class MinecartRenderStateMixin implements MinecartBannerable {
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
