package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0.4
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Shadow public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "getEquipmentSlotForItem", at = @At(value = "HEAD"), cancellable = true)
	private void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
		if (MoreBannerFeatures.isTrinketsInstalled()) return;
		if (!(this instanceof Bannerable)) return;
		Item item = stack.getItem();

		if (item instanceof BannerItem) {
			if (getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
				cir.setReturnValue(EquipmentSlot.CHEST);
			} else {
				cir.setReturnValue(EquipmentSlot.HEAD);
			}
		}
	}

	@Inject(method = "isEquippableInSlot", at = @At(value = "HEAD"), cancellable = true)
	private void isEquippableInSlot(ItemStack itemStack, EquipmentSlot equipmentSlot, CallbackInfoReturnable<Boolean> cir) {
		if (MoreBannerFeatures.isTrinketsInstalled()) return;
		if (!(this instanceof Bannerable)) return;
		Item item = itemStack.getItem();

		if (item instanceof BannerItem) {
			cir.setReturnValue(equipmentSlot == EquipmentSlot.CHEST || equipmentSlot == EquipmentSlot.HEAD);
		}
	}


}
