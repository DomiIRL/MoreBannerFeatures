package dev.svrt.domiirl.mbf.layer.side;

import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * Banner position provider for ghast entities.
 */
public class GhastBannerPositionProvider implements BannerPositionProvider {

    @Override
    public BannerPosition getPosition(LivingEntityRenderState state) {
        float xOffset = -10F;
        float yOffset = -100F;
        float zOffset = 35F;

        if (MBFOptions.ALTERNATE_HAPPY_GHAST.getBooleanValue()) {
            yOffset = -72F;
            zOffset = 39F;
        }

        float scale = 3F;
        float width = 1F;

        return new BannerPosition(xOffset, yOffset, zOffset, width, scale);
    }
}

