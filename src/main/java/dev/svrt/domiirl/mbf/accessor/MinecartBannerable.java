package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;

public interface MinecartBannerable extends Bannerable {

  @Override
  default boolean moreBannerFeatures$isEnabled() {
    return MBFOptions.MINECART_BANNERS.getBooleanValue();
  }
}
