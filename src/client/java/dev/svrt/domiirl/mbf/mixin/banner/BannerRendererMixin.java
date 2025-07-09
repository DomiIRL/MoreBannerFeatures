package dev.svrt.domiirl.mbf.mixin.banner;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.RendererUtils;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0.2
 */
@Mixin(value = BannerRenderer.class, priority = 10000)
public abstract class BannerRendererMixin implements BlockEntityRenderer<BannerBlockEntity>  {

	@Shadow @Final private BannerModel wallModel;

	@Shadow @Final private BannerFlagModel wallFlagModel;

	@Shadow @Final private BannerFlagModel standingFlagModel;

	@Inject(
		method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/world/phys/Vec3;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderBanner(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IIFLnet/minecraft/client/model/BannerModel;Lnet/minecraft/client/model/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)V"
		),
		cancellable = true
	)
	private void onRender(BannerBlockEntity bannerBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, Vec3 vec3, CallbackInfo ci) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		BlockState state = bannerBlockEntity.getBlockState();
		if (state.getBlock() instanceof BannerBlock && state.getValue(BlockStateProperties.HANGING)) {
			float g = -RotationSegment.convertToDegrees(state.getValue(BannerBlock.ROTATION));
			long l = bannerBlockEntity.getLevel().getGameTime();
			BlockPos blockPos = bannerBlockEntity.getBlockPos();
			float h = ((float)Math.floorMod(blockPos.getX() * 7L + blockPos.getY() * 9L + blockPos.getZ() * 13L + l, 100L) + f) / 100.0F;
			poseStack.translate(0.0D, -.85D, 0.0D);
			RendererUtils.renderBanner(
				poseStack, multiBufferSource, i, j, g,
				RendererUtils.BANNER_BAR, this.standingFlagModel, h,
				bannerBlockEntity.getBaseColor(), bannerBlockEntity.getPatterns()
			);
			ci.cancel();
		}
	}

	@ModifyArgs(method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private static void modifyArgs(Args args) {
		if (RendererUtils.nextBannerGlint) {
			RendererUtils.nextBannerGlint = false;
			args.set(3, true);
		}
	}

}
