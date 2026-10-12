package dev.svrt.domiirl.mbf.mixin.common;

//? if <1.21.4 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import dev.svrt.domiirl.mbf.utils.BannerItems;
import dev.svrt.domiirl.mbf.utils.MBFUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Before item models there is no model to swap, so the head item itself becomes the banner it shows
@Mixin(CustomHeadLayer.class)
public class CustomHeadLayerMixin {

	@ModifyVariable(
		method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
		at = @At("STORE"),
		ordinal = 0
	)
	private ItemStack mbf$bannerOnHead(ItemStack headItem, PoseStack poseStack, SubmitNodeCollector buffer, int light, LivingEntity entity) {
		if (headItem.getItem() instanceof BannerItem) {
			return headItem;
		}

		ItemStack itemStack = MBFUtils.getHeadItem(entity);
		return itemStack.getItem() instanceof BannerItem || BannerItems.isDecorated(itemStack) ? BannerItems.asBanner(itemStack) : headItem;
	}
}
*///?}
