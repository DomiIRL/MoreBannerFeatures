package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.gui.BannerSlot;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
//? if >=1.21.11 {
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
//?} else {
/*import net.minecraft.world.inventory.HorseInventoryMenu;
*///?}
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.21.11 {
@Mixin(AbstractMountInventoryMenu.class)
//?} else {
/*@Mixin(HorseInventoryMenu.class)
*///?}
public abstract class AbstractMountInventoryMenuMixin extends AbstractContainerMenu {

	//? if >=1.21.11 {
	@Shadow @Final protected Container mountContainer;
	//?} else {
	/*@Shadow @Final private Container horseContainer;
	*///?}

	protected AbstractMountInventoryMenuMixin(@Nullable MenuType<?> type, int syncId) {
		super(type, syncId);
	}

	// The banner slot is appended last, so it is the end of the list
	@Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
	private void onQuickMoveStack(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
		int bannerSlotIndex = this.slots.size() - 1;
		if (!(this.slots.get(bannerSlotIndex) instanceof BannerSlot)) {
			return;
		}

		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemStack = slot.getItem();
			ItemStack original = itemStack.copy();

			if (index == bannerSlotIndex) {
				//? if >=1.21.11 {
				int mountSlots = this.mountContainer.getContainerSize();
				//?} else {
				/*int mountSlots = this.horseContainer.getContainerSize();
				*///?}
				if (!this.moveItemStackTo(itemStack, 2 + mountSlots, bannerSlotIndex, true)) {
					cir.setReturnValue(ItemStack.EMPTY);
					return;
				}
				slot.setChanged();
				cir.setReturnValue(original);
				return;
			}

			if (this.getSlot(bannerSlotIndex).mayPlace(itemStack) && !this.getSlot(bannerSlotIndex).hasItem()) {
				ItemStack moved = itemStack.copy();
				if (!this.moveItemStackTo(itemStack, bannerSlotIndex, bannerSlotIndex + 1, false)) {
					cir.setReturnValue(ItemStack.EMPTY);
					return;
				}
				slot.setChanged();
				cir.setReturnValue(moved);
			}
		}
	}
}
