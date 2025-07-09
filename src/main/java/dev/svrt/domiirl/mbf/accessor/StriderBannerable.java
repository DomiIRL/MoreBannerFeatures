package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;

public interface StriderBannerable extends Bannerable {

  @Override
  default boolean moreBannerFeatures$isEnabled() {
    return MBFOptions.STRIDER_BANNERS.getBooleanValue();
  }
}
