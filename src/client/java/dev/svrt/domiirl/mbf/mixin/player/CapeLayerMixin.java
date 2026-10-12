package dev.svrt.domiirl.mbf.mixin.player;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
//?} else {
/*import net.minecraft.client.player.AbstractClientPlayer;
*///?}

@Mixin(CapeLayer.class)
//? if >=1.21.2 {
public abstract class CapeLayerMixin extends RenderLayer<AvatarRenderState, PlayerModel> {
//?} else {
/*public abstract class CapeLayerMixin extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
*///?}

	//? if >=1.21.2 {
	public CapeLayerMixin(RenderLayerParent<AvatarRenderState, PlayerModel> context) {
	//?} else {
	/*public CapeLayerMixin(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> context) {
	*///?}
		super(context);
	}

	//? if >=1.21.2 {
	@Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At(value = "HEAD"), cancellable = true)
	private void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, AvatarRenderState avatarRenderState, float f, float g, CallbackInfo ci) {
	//?} else {
	/*@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "HEAD"), cancellable = true)
	private void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, AbstractClientPlayer avatarRenderState, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
	*///?}
		if (avatarRenderState instanceof Bannerable bannerable && RendererUtils.isLegitPlayerBannerEquipment(bannerable.mbf$getBannerItem())) {
			ci.cancel();
		}
	}
}
