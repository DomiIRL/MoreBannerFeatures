package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;

public interface GhastBannerable extends Bannerable {

  @Override
  default boolean moreBannerFeatures$isEnabled() {
    return MBFOptions.HAPPY_GHAST_BANNERS.getBooleanValue();
  }
}
