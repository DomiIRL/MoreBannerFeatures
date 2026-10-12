package dev.svrt.domiirl.mbf.gui;

//? if <1.21.5 {
/*import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ticks.ContainerSingleItem;

// Mob.createEquipmentSlotContainer only arrived in 1.21.5; this is the same container for the versions before
public record EquipmentSlotContainer(Mob mob, EquipmentSlot slot) implements ContainerSingleItem {

	@Override
	public ItemStack getTheItem() {
		return this.mob.getItemBySlot(this.slot);
	}

	@Override
	public void setTheItem(ItemStack itemStack) {
		this.mob.setItemSlot(this.slot, itemStack);
		if (!itemStack.isEmpty()) {
			this.mob.setGuaranteedDrop(this.slot);
			this.mob.setPersistenceRequired();
		}
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(Player player) {
		return player.getVehicle() == this.mob || player.canInteractWithEntity(this.mob, 4.0D);
	}
}
*///?}
