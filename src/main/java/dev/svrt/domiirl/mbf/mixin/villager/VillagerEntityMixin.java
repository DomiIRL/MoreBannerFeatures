package dev.svrt.domiirl.mbf.mixin.villager;

import dev.svrt.domiirl.mbf.accessor.VillagerBannerable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.1
 */
@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager implements VillagerBannerable {

	@Shadow public abstract boolean canBreed();

	public VillagerEntityMixin(EntityType<? extends AbstractVillager> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @NotNull ItemStack moreBannerFeatures$getBannerItem() {
		return getItemBySlot(EquipmentSlot.HEAD);
	}

	@Override
	public void moreBannerFeatures$setBannerItem(@NotNull ItemStack itemStack) {
		this.setItemSlot(EquipmentSlot.HEAD, itemStack);
	}

	@Override
	protected void dropEquipment(ServerLevel serverLevel) {
		if (!moreBannerFeatures$getBannerItem().isEmpty()) {
			spawnAtLocation(serverLevel, moreBannerFeatures$getBannerItem());
		}
		super.dropEquipment(serverLevel);
	}

}
