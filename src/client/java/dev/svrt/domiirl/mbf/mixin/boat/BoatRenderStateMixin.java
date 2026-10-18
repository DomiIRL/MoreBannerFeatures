package dev.svrt.domiirl.mbf.mixin.boat;

//? if >=1.21.2 {
import dev.svrt.domiirl.mbf.accessor.BoatBannerable;
import dev.svrt.domiirl.mbf.accessor.BoatChestRenderState;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatRenderState.class)
public class BoatRenderStateMixin implements BoatBannerable, BoatChestRenderState {
  public ItemStack bannerItem = ItemStack.EMPTY;
  public boolean hasChest;

  @Override
  public boolean mbf$hasChest() {
    return hasChest;
  }

  @Override
  public void mbf$setHasChest(boolean hasChest) {
    this.hasChest = hasChest;
  }

  @Override
  public @NotNull ItemStack mbf$getBannerItem() {
    return bannerItem;
  }

  @Override
  public void mbf$setBannerItem(@NotNull ItemStack stack) {
    this.bannerItem = stack;
  }
}
//?}
