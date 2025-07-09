package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;

public interface HorseBannerable extends Bannerable {

  @Override
  default boolean moreBannerFeatures$isEnabled() {
    return MBFOptions.HORSE_BANNERS.getBooleanValue();
  }
}
