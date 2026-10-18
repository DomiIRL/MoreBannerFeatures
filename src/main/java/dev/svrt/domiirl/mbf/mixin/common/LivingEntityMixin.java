package dev.svrt.domiirl.mbf.mixin.common;

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

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Shadow public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	//? if >=1.21 {
	@Inject(method = "getEquipmentSlotForItem", at = @At(value = "HEAD"), cancellable = true)
	private void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
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
	//?} else {
	/*// Static before 1.21, so there is no entity to look at: a banner always goes on as a cape, like MBF 1.x
	@Inject(method = "getEquipmentSlotForItem", at = @At(value = "HEAD"), cancellable = true)
	private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
		if (stack.getItem() instanceof BannerItem) {
			cir.setReturnValue(EquipmentSlot.CHEST);
		}
	}
	*///?}

	// The armor slots ask this from 1.21.5, before that HelmetSlotMixin opens the helmet slot
	//? if >=1.21.2 {
	@Inject(method = "isEquippableInSlot", at = @At(value = "HEAD"), cancellable = true)
	private void isEquippableInSlot(ItemStack itemStack, EquipmentSlot equipmentSlot, CallbackInfoReturnable<Boolean> cir) {
		if (!(this instanceof Bannerable)) return;
		Item item = itemStack.getItem();

		if (item instanceof BannerItem) {
			cir.setReturnValue(equipmentSlot == EquipmentSlot.CHEST || equipmentSlot == EquipmentSlot.HEAD);
		}
	}
	//?}


}
