//? if <1.21.2 {
/*package dev.svrt.domiirl.mbf.mixin.player;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >=1.21 {
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
//?} else {
/^import net.minecraft.world.entity.player.Inventory;
^///?}

// The player's armor slots ask which slot an item belongs in, and a banner belongs in the chest slot until
// 1.21.2 lets an item fit several. This lets the helmet slot take a banner as well.
//? if >=1.21 {
// ArmorSlot is package-private
@Mixin(targets = "net.minecraft.world.inventory.ArmorSlot")
//?} else {
/^// The armor slots are an anonymous class in InventoryMenu until 1.21
@Mixin(targets = "net.minecraft.world.inventory.InventoryMenu$1")
^///?}
public abstract class HelmetSlotMixin extends Slot {

	//? if >=1.21 {
	@Shadow @Final private EquipmentSlot slot;
	//?}

	protected HelmetSlotMixin(Container container, int index, int x, int y) {
		super(container, index, x, y);
	}

	@Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
	private void mayPlaceBanner(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		//? if >=1.21 {
		boolean helmet = this.slot == EquipmentSlot.HEAD;
		//?} else {
		/^boolean helmet = this.getContainerSlot() == Inventory.INVENTORY_SIZE + EquipmentSlot.HEAD.getIndex();
		^///?}
		if (helmet && stack.getItem() instanceof BannerItem) {
			cir.setReturnValue(true);
		}
	}
}
*///?}
