package dev.svrt.domiirl.mbf.gui;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
//? if <1.21.2 {
/*import com.mojang.datafixers.util.Pair;
import net.minecraft.world.inventory.InventoryMenu;
*///?}

public class BannerSlot extends Slot {

	protected final Entity entity;
	private final Identifier slotIcon;

	public BannerSlot(Entity entity, Container inventory, int index, int x, int y, Identifier slotIcon) {
		super(inventory, index, x, y);
		this.entity = entity;
		this.slotIcon = slotIcon;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof BannerItem && !this.hasItem() && entity instanceof Bannerable;
	}

	@Override
	public boolean isActive() {
		return entity instanceof Bannerable;
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	//? if >=1.21.2 {
	@Override
	public @Nullable Identifier getNoItemIcon() {
		return slotIcon;
	}
	//?} else {
	/*// Slot icons come from the block atlas before 1.21.2, which picks up textures/item of every namespace
	@Override
	public Pair<Identifier, Identifier> getNoItemIcon() {
		return Pair.of(InventoryMenu.BLOCK_ATLAS, Identifier.fromNamespaceAndPath(slotIcon.getNamespace(), "item/empty_slot_banner"));
	}
	*///?}
}
