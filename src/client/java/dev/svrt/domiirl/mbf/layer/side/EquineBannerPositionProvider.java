
package dev.svrt.domiirl.mbf.layer.side;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?} else {
/*import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
*///?}

/**
 * Banner position provider for horse-like entities.
 */
public class EquineBannerPositionProvider implements BannerPositionProvider {

  private final boolean undead;

  public EquineBannerPositionProvider(boolean undead) {
    this.undead = undead;
  }

  @Override
  //? if >=1.21.2 {
  public BannerPosition getPosition(LivingEntityRenderState state) {
  //?} else {
  /*public BannerPosition getPosition(LivingEntity state) {
  *///?}
    float xOffset = 0.0F;
    float yOffset = 0F;
    float zOffset = 0F;
    float scale = 0.6F;
    float width = 1F;

    //? if >=1.21.2 {
    if (state instanceof EquineRenderState equineRenderState) {
      //? if >=1.21.5 {
      boolean saddled = !equineRenderState.saddle.isEmpty();
      //?} else {
      /*boolean saddled = equineRenderState.isSaddled;
      *///?}
      boolean chested = state instanceof DonkeyRenderState;
      boolean hasChest = state instanceof DonkeyRenderState donkeyState && donkeyState.hasChest;
    //?} else {
    /*if (state instanceof AbstractHorse horse) {
      boolean saddled = horse.isSaddled();
      boolean chested = horse instanceof AbstractChestedHorse;
      boolean hasChest = horse instanceof AbstractChestedHorse chestedHorse && chestedHorse.hasChest();
    *///?}

      if (chested) {
        xOffset = hasChest ? -20 : -11.25F;
        yOffset = -35F;
        zOffset = -5F;

        if (saddled && !hasChest) {
          yOffset += 0.7F;
          zOffset += 0.8F;
        }
      } else if (undead) {
        xOffset = -11F;
        yOffset = -30.8F;
        zOffset = -3.8F;

        if (saddled) {
          zOffset += 0.7F;
          yOffset += 0.8F;
        }
      } else {
        xOffset = -11F;
        yOffset = -27.7F;
        zOffset = -3F;

        if (saddled) {
          zOffset += 0.7F;
          yOffset += 0.8F;
        }
      }
    }

    return new BannerPosition(xOffset, yOffset, zOffset, width, scale);
  }
}
