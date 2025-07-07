package dev.svrt.domiirl.mbf.mixin.villager;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.1
 */
@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager implements Bannerable {

	@Shadow public abstract InteractionResult mobInteract(Player player, InteractionHand hand);

	@Shadow public abstract boolean canBreed();

	@Shadow public abstract void setGossips(GossipContainer gossipContainer);

	public VillagerEntityMixin(EntityType<? extends AbstractVillager> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @NotNull ItemStack getBannerItem() {
		return getItemBySlot(EquipmentSlot.HEAD);
	}

	public void setBannerItem(ItemStack itemStack) {
		this.setItemSlot(EquipmentSlot.HEAD, itemStack);
	}

	@Override
	protected void dropEquipment(ServerLevel serverLevel) {
		if (!getBannerItem().isEmpty()) {
			spawnAtLocation(serverLevel, getBannerItem());
		}
		super.dropEquipment(serverLevel);
	}

	@Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
	private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

		if (!MBFOptions.VILLAGER_BANNERS.getBooleanValue()) {
			return;
		}

		if (player.isSecondaryUseActive()) return;

		ItemStack itemStack = player.getItemInHand(hand);
		ItemStack bannerItem = getBannerItem();
		if (itemStack.getItem() instanceof BannerItem) {
			if (ItemStack.isSameItem(bannerItem, itemStack)) return;

			this.level().playSound(null, this, SoundEvents.HORSE_STEP_WOOD, SoundSource.PLAYERS, 1.0F, 1.0F);

			if (!bannerItem.isEmpty()) {
				if (this.level() instanceof ServerLevel serverLevel) {
					spawnAtLocation(serverLevel, bannerItem);
				}
			}

			ItemStack copy = itemStack.copy();
			copy.setCount(1);
			setBannerItem(copy);

			if (!player.getAbilities().instabuild) {
				itemStack.shrink(1);
			}

			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		} else if (itemStack.getItem() instanceof ShearsItem) {
			this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
			if (!this.isClientSide()) {
				this.gameEvent(GameEvent.SHEAR, player);
				itemStack.hurtAndBreak(1, player, getSlotForHand(hand));
			}

			if (level() instanceof ServerLevel serverLevel) {
				spawnAtLocation(serverLevel, bannerItem);
			}
			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		}

	}

}
