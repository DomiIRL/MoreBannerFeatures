package dev.svrt.domiirl.morebannerfeatures.mixin.pig;

import dev.svrt.domiirl.morebannerfeatures.renderer.PigBannerFeatureRenderer;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(PigRenderer.class)
public abstract class PigRendererMixin extends MobRenderer<Pig, PigRenderState, PigModel> {

	public PigRendererMixin(Context context, PigModel<Pig> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		addLayer(new PigBannerFeatureRenderer(this));
	}

}
