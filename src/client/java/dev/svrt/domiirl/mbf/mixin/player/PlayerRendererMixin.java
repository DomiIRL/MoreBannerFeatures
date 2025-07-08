package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.accessor.BannerRenderState;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.feature.BannerCapeFeatureRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0.4
 */
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {

	public PlayerRendererMixin(Context context, PlayerModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, boolean slim, CallbackInfo ci) {
		addLayer(new BannerCapeFeatureRenderer(this, ctx.getModelSet(), ctx.getEquipmentAssets()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("HEAD"))
	private void extractRenderState(AbstractClientPlayer player, PlayerRenderState renderState, float f, CallbackInfo ci) {
		if (renderState instanceof BannerRenderState bannerRenderState && player instanceof Bannerable bannerable) {
			bannerRenderState.setBannerItem(bannerable.getBannerItem());
		}
	}
}
