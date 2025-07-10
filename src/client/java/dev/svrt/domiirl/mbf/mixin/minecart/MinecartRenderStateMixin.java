package dev.svrt.domiirl.mbf.mixin.minecart;

import dev.svrt.domiirl.mbf.accessor.BannerableMinecartRenderState;
import dev.svrt.domiirl.mbf.accessor.MinecartBannerable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.client.renderer.entity.state.MinecartRenderState.class)
public class MinecartRenderStateMixin implements MinecartBannerable, BannerableMinecartRenderState {
  public ItemStack bannerItem = ItemStack.EMPTY;
  public Vec3 velocity = Vec3.ZERO;

  @Override
  public @NotNull ItemStack moreBannerFeatures$getBannerItem() {
    return bannerItem;
  }

  @Override
  public void moreBannerFeatures$setBannerItem(@NotNull ItemStack stack) {
    this.bannerItem = stack;
  }

  @Override
  public Vec3 getVelocity() {
    return velocity;
  }

  @Override
  public void setVelocity(Vec3 velocity) {
    this.velocity = velocity;
  }
}

