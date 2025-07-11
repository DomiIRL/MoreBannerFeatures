package dev.svrt.domiirl.mbf.accessor;

import dev.svrt.domiirl.mbf.config.MBFOptions;

public interface StriderBannerable extends Bannerable {

  @Override
  default boolean mbf$isEnabled() {
    return MBFOptions.STRIDER_BANNERS.getBooleanValue();
  }
}
