package dev.svrt.domiirl.mbf.mixin.threads;

import dev.svrt.domiirl.mbf.registry.ModDataComponents;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LoomMenu.class)
public class LoomMenuMixin {

  @Shadow @Final
  Slot bannerSlot;

  @ModifyConstant(method = "slotsChanged", constant = @Constant(intValue = 6, ordinal = 0))
  public int getLimit(int constant) {
    ItemStack bannerItem = this.bannerSlot.getItem();
    return bannerItem.getOrDefault(ModDataComponents.MAX_BANNER_LAYERS, 6);
  }


}
