package dev.svrt.domiirl.mbf.mixin.player;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0.4
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Bannerable {

	public PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @NotNull ItemStack moreBannerFeatures$getBannerItem() {
		return getItemBySlot(EquipmentSlot.CHEST);
	}

	@Override
	public void moreBannerFeatures$setBannerItem(@NotNull ItemStack stack) {
		this.setItemSlot(EquipmentSlot.CHEST, stack);
	}
}
