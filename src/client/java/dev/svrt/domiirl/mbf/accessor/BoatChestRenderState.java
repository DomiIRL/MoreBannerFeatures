//? if >=1.21.2 {
package dev.svrt.domiirl.mbf.accessor;

// Whether the boat being rendered carries a chest, which takes the stern the banner otherwise stands on
public interface BoatChestRenderState {
  boolean mbf$hasChest();

  void mbf$setHasChest(boolean hasChest);
}
//?}
