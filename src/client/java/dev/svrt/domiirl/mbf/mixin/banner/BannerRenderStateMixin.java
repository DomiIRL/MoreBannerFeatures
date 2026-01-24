package dev.svrt.domiirl.mbf.mixin.banner;

import dev.svrt.domiirl.mbf.accessor.BannerRenderStateAccessor;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BannerRenderState.class)
public class BannerRenderStateMixin implements BannerRenderStateAccessor {
  public boolean hanging;

  @Override
  public boolean mbf$isHanging() {
    return hanging;
  }

  @Override
  public void mbf$setHanging(boolean hanging) {
    this.hanging = hanging;
  }
}
