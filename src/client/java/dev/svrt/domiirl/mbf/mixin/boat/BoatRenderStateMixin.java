package dev.svrt.domiirl.mbf.mixin.boat;

import dev.svrt.domiirl.mbf.accessor.BoatBannerable;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatRenderState.class)
public class BoatRenderStateMixin implements BoatBannerable {
  public ItemStack bannerItem = ItemStack.EMPTY;

  @Override
  public @NotNull ItemStack mbf$getBannerItem() {
    return bannerItem;
  }

  @Override
  public void mbf$setBannerItem(@NotNull ItemStack stack) {
    this.bannerItem = stack;
  }
}
