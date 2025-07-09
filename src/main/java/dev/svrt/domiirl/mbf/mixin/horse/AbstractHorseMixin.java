package dev.svrt.domiirl.mbf.mixin.horse;

import dev.svrt.domiirl.mbf.accessor.HorseBannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal implements HorseBannerable {

	protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @NotNull ItemStack moreBannerFeatures$getBannerItem() {
		return this.getItemBySlot(EquipmentSlot.CHEST);
	}

	@Override
	public void moreBannerFeatures$setBannerItem(@NotNull ItemStack stack) {
		this.setItemSlot(EquipmentSlot.CHEST, stack);
	}

	@Override
	public boolean moreBannerFeatures$isEnabled() {
		return MBFOptions.HORSE_BANNERS.getBooleanValue();
	}
}
