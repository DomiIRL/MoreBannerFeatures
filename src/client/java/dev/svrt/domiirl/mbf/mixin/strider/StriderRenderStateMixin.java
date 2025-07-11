package dev.svrt.domiirl.mbf.mixin.strider;

import dev.svrt.domiirl.mbf.accessor.StriderBannerable;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StriderRenderState.class)
public class StriderRenderStateMixin implements StriderBannerable {
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
