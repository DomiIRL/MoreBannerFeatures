package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AvatarRenderState.class)
public class PlayerRenderStateMixin implements Bannerable {
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
