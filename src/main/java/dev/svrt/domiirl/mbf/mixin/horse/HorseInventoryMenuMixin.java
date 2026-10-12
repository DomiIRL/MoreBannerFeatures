package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import dev.svrt.domiirl.mbf.gui.BannerSlot;
//? if <1.21.5 {
/*import dev.svrt.domiirl.mbf.gui.EquipmentSlotContainer;
*///?}
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseInventoryMenu.class)
public abstract class HorseInventoryMenuMixin extends AbstractContainerMenu {

	@Unique
	private static final Identifier SLOT_ICON = MoreBannerFeatures.id("container/slot/banner");

	protected HorseInventoryMenuMixin(@Nullable MenuType<?> type, int syncId) {
		super(type, syncId);
	}

	// TAIL so the banner slot lands after the player inventory
	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void init(int syncId, Inventory playerInventory, Container inventory, AbstractHorse entity, int j, CallbackInfo ci) {
		if (entity instanceof Bannerable && MBFOptions.HORSE_SLOT.getBooleanValue()) {
			//? if >=1.21.5 {
			Container container2 = entity.createEquipmentSlotContainer(EquipmentSlot.CHEST);
			//?} else {
			/*Container container2 = new EquipmentSlotContainer(entity, EquipmentSlot.CHEST);
			*///?}
			this.addSlot(new BannerSlot(entity, container2, 0, 8, 54, SLOT_ICON));
		}
	}
}
