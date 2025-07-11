package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;

public interface VillagerBannerable extends Bannerable {

  @Override
  default boolean mbf$isEnabled() {
    return MBFOptions.VILLAGER_BANNERS.getBooleanValue();
  }
}
