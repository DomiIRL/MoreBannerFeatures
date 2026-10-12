package dev.svrt.domiirl.mbf.mixin.fox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.svrt.domiirl.mbf.Poses;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.model.animal.fox.FoxModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.FoxRenderState;
//?} else {
/*import net.minecraft.world.entity.animal.Fox;
*///?}

@Mixin(FoxHeldItemLayer.class)
//? if >=1.21.2 {
public abstract class FoxHeldItemLayerMixin extends RenderLayer<FoxRenderState, FoxModel> {
//?} else {
/*public abstract class FoxHeldItemLayerMixin extends RenderLayer<Fox, FoxModel<Fox>> {
*///?}

	//? if >=1.21.2 {
	public FoxHeldItemLayerMixin(RenderLayerParent<FoxRenderState, FoxModel> renderLayerParent) {
	//?} else {
	/*public FoxHeldItemLayerMixin(RenderLayerParent<Fox, FoxModel<Fox>> renderLayerParent) {
	*///?}
		super(renderLayerParent);
	}

	//? if >=1.21.2 {
	@Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/FoxRenderState;FF)V",
		//? if >=1.21.9 {
		at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"))
		//?} else {
		/*at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V"))
		*///?}
	private void renderItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, FoxRenderState foxRenderState, float f, float g, CallbackInfo ci) {
	//?} else {
	/*@Inject(
		method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/world/entity/animal/Fox;FFFFFF)V",
		//? if >=1.19.4 {
		at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V")
		//?} else {
		/^at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V")
		^///?}
	)
	private void renderItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, Fox fox, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
	*///?}
		if (MBFOptions.FOX_CORRECTION.getBooleanValue()) {
			Poses.rotateDegrees(poseStack, Axis.ZP, 180);
		}
	}

}
