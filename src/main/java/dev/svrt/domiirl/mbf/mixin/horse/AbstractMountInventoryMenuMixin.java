package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.gui.BannerSlot;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
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

@Mixin(AbstractMountInventoryMenu.class)
public abstract class AbstractMountInventoryMenuMixin extends AbstractContainerMenu {

	@Shadow @Final protected Container mountContainer;

	protected AbstractMountInventoryMenuMixin(@Nullable MenuType<?> type, int syncId) {
		super(type, syncId);
	}

	// HorseInventoryMenuMixin appends the banner slot last, so its index is the end of the list.
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
				if (!this.moveItemStackTo(itemStack, 2 + this.mountContainer.getContainerSize(), bannerSlotIndex, true)) {
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
