package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.layer.BannerCapeFeatureRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<Avatar, AvatarRenderState, PlayerModel> {

	public PlayerRendererMixin(Context context, PlayerModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, boolean slim, CallbackInfo ci) {
		addLayer(new BannerCapeFeatureRenderer(this, ctx.getMaterials(), ctx.getEquipmentAssets()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("HEAD"))
	private void extractRenderState(Avatar avatar, AvatarRenderState renderState, float f, CallbackInfo ci) {
		if (renderState instanceof Bannerable bannerRenderState && avatar instanceof Bannerable bannerable) {
			bannerRenderState.mbf$setBannerItem(bannerable.mbf$getBannerItem());
		}
	}
}
