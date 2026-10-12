package dev.svrt.domiirl.mbf.layer.side;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?} else {
/*import net.minecraft.world.entity.LivingEntity;
*///?}

/**
 * Interface for providing banner positioning data for different entity types.
 */
public interface BannerPositionProvider {

    /**
     * Get the banner position configuration for an entity render state.
     *
     * @param state The entity render state
     * @return The banner position configuration
     */
    //? if >=1.21.2 {
    BannerPosition getPosition(LivingEntityRenderState state);
    //?} else {
    /*BannerPosition getPosition(LivingEntity state);
    *///?}

    /**
     * Class representing banner position configuration.
     */
    record BannerPosition(float xOffset, float yOffset, float zOffset, float entityWidth, float scale) {
    }
}
