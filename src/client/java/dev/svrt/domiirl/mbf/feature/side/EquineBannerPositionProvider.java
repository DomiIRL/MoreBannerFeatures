
package dev.svrt.domiirl.mbf.feature.side;

import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * Banner position provider for horse-like entities.
 */
public class EquineBannerPositionProvider implements BannerPositionProvider {

  private final boolean undead;

  public EquineBannerPositionProvider(boolean undead) {
    this.undead = undead;
  }

  @Override
  public BannerPosition getPosition(LivingEntityRenderState state) {
    float xOffset = 0.0F;
    float yOffset = 0F;
    float zOffset = 0F;
    float scale = 0.6F;
    float width = 1F;

    if (state instanceof EquineRenderState equineRenderState) {

      if (state instanceof DonkeyRenderState donkeyState) {
        xOffset = donkeyState.hasChest ? -20 : -11.25F;
        yOffset = -35F;
        zOffset = -5F;

        if (!equineRenderState.saddle.isEmpty() && !donkeyState.hasChest) {
          yOffset += 0.7F;
          zOffset += 0.8F;
        }
      } else if (undead) {
        xOffset = -11F;
        yOffset = -30.8F;
        zOffset = -3.8F;

        if (!equineRenderState.saddle.isEmpty()) {
          zOffset += 0.7F;
          yOffset += 0.8F;
        }
      } else {
        xOffset = -11F;
        yOffset = -27.7F;
        zOffset = -3F;

        if (!equineRenderState.saddle.isEmpty()) {
          zOffset += 0.7F;
          yOffset += 0.8F;
        }
      }
    }

    return new BannerPosition(xOffset, yOffset, zOffset, width, scale);
  }
}
