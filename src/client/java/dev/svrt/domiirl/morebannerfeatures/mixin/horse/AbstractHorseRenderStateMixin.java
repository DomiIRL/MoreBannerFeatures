package dev.svrt.domiirl.morebannerfeatures.mixin.horse;

import dev.svrt.domiirl.morebannerfeatures.accessor.BannerRenderState;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EquineRenderState.class)
public class AbstractHorseRenderStateMixin extends LivingEntityRenderState implements SideBannerable, BannerRenderState {

  @Shadow public ItemStack saddle;
  public ItemStack bannerItem = ItemStack.EMPTY;

  @Override
  public void setBannerItem(ItemStack stack) {
    this.bannerItem = stack;
  }

  @Override
  public ItemStack getBannerItem() {
    return this.bannerItem;
  }

  @Override
  public float getYOffset() {
    return !saddle.isEmpty() ? 0.07F : 0;
  }

  @Override
  public float getXOffset() {
    return !saddle.isEmpty() ? 0.06F : 0;
  }
}
