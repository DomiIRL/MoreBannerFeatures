package dev.svrt.domiirl.mbf.gui;

//? if <1.21.5 {
/*import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// Mob.createEquipmentSlotContainer only arrived in 1.21.5; this is the same container for the versions before.
// A plain Container, since the single item interface changed shape in 1.20.3, 1.20.5 and 1.21
public record EquipmentSlotContainer(Mob mob, EquipmentSlot slot) implements Container {

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.mob.getItemBySlot(this.slot).isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return this.mob.getItemBySlot(this.slot);
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		ItemStack split = this.getItem(index).split(amount);
		if (this.getItem(index).isEmpty()) {
			this.setItem(index, ItemStack.EMPTY);
		}
		return split;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack itemStack = this.getItem(index);
		this.setItem(index, ItemStack.EMPTY);
		return itemStack;
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
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
		return player.getVehicle() == this.mob || (this.mob.isAlive() && player.distanceTo(this.mob) < 8.0F);
	}

	@Override
	public void clearContent() {
		this.setItem(0, ItemStack.EMPTY);
	}
}
*///?}
