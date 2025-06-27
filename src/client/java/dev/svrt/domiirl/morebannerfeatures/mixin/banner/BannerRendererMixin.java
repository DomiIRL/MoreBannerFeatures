package dev.svrt.domiirl.morebannerfeatures.mixin.banner;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.morebannerfeatures.core.config.MBFOptions;
import dev.svrt.domiirl.morebannerfeatures.RendererUtils;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

	private BannerModel hangingModel;

	@Inject(method = "<init>(Lnet/minecraft/client/model/geom/EntityModelSet;)V", at = @At("TAIL"))
	public void init(EntityModelSet entityModelSet, CallbackInfo ci) {
		this.hangingModel = new BannerModel(entityModelSet.bakeLayer(ModelLayers.STANDING_BANNER));
	}

	@Inject(method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/world/phys/Vec3;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;standingModel:Lnet/minecraft/client/model/BannerModel;"))
	private void render(BannerBlockEntity bannerBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, Vec3 vec3, CallbackInfo ci) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		BlockState blockState = bannerBlockEntity.getBlockState();

		try {
			if (blockState.getBlock() instanceof BannerBlock && blockState.getValue(BlockStateProperties.HANGING)) {
				poseStack.translate(0.0D, -2.5D, 0.0D);
				this.pole.visible = false;
			}
		} catch (Exception ex) {
			// There is a banner from before the mod was downloaded.
		}

	}

	@ModifyArgs(method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;ZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private static void modifyArgs(Args args) {
		if (RendererUtils.nextBannerGlint) {
			RendererUtils.nextBannerGlint = false;
			args.set(3, true);
		} else if (!((boolean) args.get(2))) {
			args.set(3, MBFOptions.BANNER_GLINT.getBooleanValue());
		}
	}

}
