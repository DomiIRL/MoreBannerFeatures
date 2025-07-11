package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.accessor.HorseBannerable;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EquineRenderState.class)
public class AbstractHorseRenderStateMixin extends LivingEntityRenderState implements HorseBannerable {

  public ItemStack bannerItem = ItemStack.EMPTY;

  @Override
  public void mbf$setBannerItem(@NotNull ItemStack stack) {
    this.bannerItem = stack;
  }

  @Override
  public @NotNull ItemStack mbf$getBannerItem() {
    return this.bannerItem;
  }
}
