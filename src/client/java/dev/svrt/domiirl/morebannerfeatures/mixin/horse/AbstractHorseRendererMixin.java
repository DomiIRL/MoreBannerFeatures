package dev.svrt.domiirl.morebannerfeatures.mixin.horse;

import dev.svrt.domiirl.morebannerfeatures.accessor.BannerRenderState;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.Bannerable;
import dev.svrt.domiirl.morebannerfeatures.renderer.HorseBaseBannerFeatureRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(AbstractHorseRenderer.class)
public abstract class AbstractHorseRendererMixin<T extends AbstractHorse, S extends EquineRenderState, M extends EntityModel<? super S>> extends AgeableMobRenderer<T, S, M> {

	public AbstractHorseRendererMixin(Context context, M entityModel, M entityModel2, float f) {
		super(context, entityModel, entityModel2, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, EntityModel entityModel, EntityModel entityModel2, CallbackInfo ci) {
		addLayer((RenderLayer<S, M>) new HorseBaseBannerFeatureRenderer((RenderLayerParent<EquineRenderState, HorseModel>) this));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;Lnet/minecraft/client/renderer/entity/state/EquineRenderState;F)V", at = @At("HEAD"), cancellable = true)
	private void extractRenderState(AbstractHorse horse, EquineRenderState state, float f, CallbackInfo ci) {
		if (state instanceof BannerRenderState bannerRenderState && horse instanceof Bannerable bannerable) {
			bannerRenderState.setBannerItem(bannerable.getBannerItem());
		}
	}

}
