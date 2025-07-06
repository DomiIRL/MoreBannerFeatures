package dev.svrt.domiirl.morebannerfeatures.mixin.camel;

import dev.svrt.domiirl.morebannerfeatures.renderer.CamelBannerFeatureRenderer;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(CamelRenderer.class)
public abstract class CamelRendererMixin extends AgeableMobRenderer<Camel, CamelRenderState, CamelModel> {

	public CamelRendererMixin(Context context, CamelModel entityModel, CamelModel entityModel2, float f) {
		super(context, entityModel, entityModel2, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		addLayer(new CamelBannerFeatureRenderer(this));
	}

}
