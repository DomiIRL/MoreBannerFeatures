package dev.svrt.domiirl.mbf.accessor;

import net.minecraft.world.phys.Vec3;

public interface BannerableMinecartRenderState {

  Vec3 getVelocity();

  void setVelocity(Vec3 velocity);

}
