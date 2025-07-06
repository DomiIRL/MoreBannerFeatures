package dev.svrt.domiirl.morebannerfeatures.mixin.llama;

import dev.svrt.domiirl.morebannerfeatures.renderer.LLamaBannerFeatureRenderer;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(LlamaRenderer.class)
public abstract class LlamaRendererMixin extends AgeableMobRenderer<Llama, LlamaRenderState, LlamaModel> {

	public LlamaRendererMixin(Context context, LlamaModel entityModel, LlamaModel entityModel2, float f) {
		super(context, entityModel, entityModel2, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, ModelLayerLocation modelLayerLocation, ModelLayerLocation modelLayerLocation2, CallbackInfo ci) {
		addLayer(new LLamaBannerFeatureRenderer(this));
	}

}
