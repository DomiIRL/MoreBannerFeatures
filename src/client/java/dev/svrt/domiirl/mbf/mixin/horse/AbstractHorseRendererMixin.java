package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.HorseBannerFeatureRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseRenderer.class)
public abstract class AbstractHorseRendererMixin<T extends AbstractHorse, S extends EquineRenderState, M extends EntityModel<? super S>> extends AgeableMobRenderer<T, S, M> {

	public AbstractHorseRendererMixin(Context context, M entityModel, M entityModel2, float f) {
		super(context, entityModel, entityModel2, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, EntityModel entityModel, EntityModel entityModel2, CallbackInfo ci) {
		addLayer((RenderLayer<S, M>) new HorseBannerFeatureRenderer((RenderLayerParent<EquineRenderState, HorseModel>) this, context.getSprites(), ((Object) this) instanceof UndeadHorseRenderer));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;Lnet/minecraft/client/renderer/entity/state/EquineRenderState;F)V", at = @At("HEAD"))
	private void extractRenderState(AbstractHorse horse, EquineRenderState state, float f, CallbackInfo ci) {
		if (state instanceof Bannerable bannerRenderState && horse instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
	}

}
