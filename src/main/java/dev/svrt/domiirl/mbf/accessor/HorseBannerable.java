package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.world.entity.EquipmentSlot;

public interface HorseBannerable extends Bannerable {

  // Horse armor holds the chest slot until 1.20.5 gives it a body slot of its own
  //? if >=1.20.5 {
  EquipmentSlot BANNER_SLOT = EquipmentSlot.CHEST;
  //?} else {
  /*EquipmentSlot BANNER_SLOT = EquipmentSlot.LEGS;
  *///?}

  @Override
  default boolean mbf$isEnabled() {
    return MBFOptions.HORSE_BANNERS.getBooleanValue();
  }
}
