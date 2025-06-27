package dev.svrt.domiirl.morebannerfeatures.core.accessor;

import net.minecraft.world.phys.Vec3;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
public interface SideBannerable extends Bannerable {

	default float getXOffset() { return 0;}
	default float getYOffset() { return 0; }
	default float getZOffset() { return 0; }
	default Vec3 getScaleOffset() { return null; }
	default boolean useStaticSwing() { return false; }
	default float getStaticSwing() { return 0; }

}
