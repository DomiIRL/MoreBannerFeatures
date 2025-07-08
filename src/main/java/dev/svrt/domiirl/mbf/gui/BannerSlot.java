package dev.svrt.domiirl.mbf.gui;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BannerSlot extends Slot {

	protected final Entity entity;
	private final ResourceLocation slotIcon;

	public BannerSlot(Entity entity, Container inventory, int index, int x, int y, ResourceLocation slotIcon) {
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

	@Override
	public @Nullable ResourceLocation getNoItemIcon() {
		return slotIcon;
	}
}
