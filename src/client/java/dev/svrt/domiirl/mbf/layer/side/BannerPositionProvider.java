package dev.svrt.domiirl.mbf.layer.side;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

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
    BannerPosition getPosition(LivingEntityRenderState state);

    /**
     * Class representing banner position configuration.
     */
    record BannerPosition(float xOffset, float yOffset, float zOffset, float entityWidth, float scale) {
    }
}
