package dev.svrt.domiirl.mbf.mixin.villager;

import dev.svrt.domiirl.mbf.accessor.VillagerBannerable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
//? if <1.21 {
/*import dev.svrt.domiirl.mbf.utils.BannerableEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?}

@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager implements VillagerBannerable {

	@Shadow public abstract boolean canBreed();

	public VillagerEntityMixin(EntityType<? extends AbstractVillager> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @NotNull ItemStack mbf$getBannerItem() {
		return getItemBySlot(EquipmentSlot.HEAD);
	}

	@Override
	public void mbf$setBannerItem(@NotNull ItemStack itemStack) {
		this.setItemSlot(EquipmentSlot.HEAD, itemStack);
	}

	//? if <1.21 {
	/*// Before 1.21 Mob reaches Entity.interact, where EntityMixin places banners, only after mobInteract, which a villager always takes
	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		BannerableEntity.handleInteract(this, player, hand, cir);
	}
	*///?}

	@Override
	protected void dropEquipment(ServerLevel serverLevel) {
		if (!mbf$getBannerItem().isEmpty()) {
			spawnAtLocation(serverLevel, mbf$getBannerItem());
		}
		super.dropEquipment(serverLevel);
	}

}
