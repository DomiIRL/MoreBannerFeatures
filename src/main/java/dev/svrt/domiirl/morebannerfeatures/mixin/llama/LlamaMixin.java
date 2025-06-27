package dev.svrt.domiirl.morebannerfeatures.mixin.llama;

import dev.svrt.domiirl.morebannerfeatures.core.accessor.InventoryBannerable;
import dev.svrt.domiirl.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(Llama.class)
public abstract class LlamaMixin extends AbstractChestedHorse implements SideBannerable, InventoryBannerable {

	public LlamaMixin(EntityType<? extends AbstractChestedHorse> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public float getXOffset() {
		return getBodyArmorItem().isEmpty() ? 0.13F : 0.2F;
	}

	@Override
	public float getYOffset() {
		return getBodyArmorItem().isEmpty() ? 0.14F : 0.21F;
	}

	@Override
	public int getTransferIndex() {
		return hasChest() ? 41 : 38;
	}

	@Override
	public int getSlot() {
		return 0;
	}

	@Override
	public float getZOffset() {
		return 0F;
	}
}
