package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;

public interface BoatBannerable extends Bannerable {

  @Override
  default boolean moreBannerFeatures$isEnabled() {
    return MBFOptions.BOAT_BANNERS.getBooleanValue();
  }
}
