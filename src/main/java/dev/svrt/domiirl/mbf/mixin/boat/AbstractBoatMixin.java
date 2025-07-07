package dev.svrt.domiirl.mbf.mixin.boat;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin extends VehicleEntity implements Leashable, Bannerable {

	private static final EntityDataAccessor<ItemStack> BANNER = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.ITEM_STACK);

	public AbstractBoatMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public @NotNull ItemStack getBannerItem() {
		if (!MBFOptions.BOAT_BANNERS.getBooleanValue()) {
			return ItemStack.EMPTY;
		}
		return this.entityData.get(BANNER);
	}

	public void setBannerItem(ItemStack itemStack) {
		this.entityData.set(BANNER, itemStack);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BANNER, ItemStack.EMPTY);
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	private void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
		input.read("Banner", ItemStack.CODEC).ifPresent(this::setBannerItem);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
		if (!getBannerItem().isEmpty()) {
			output.storeNullable("Banner", ItemStack.CODEC, getBannerItem());
		}
	}

	@Override
	protected void destroy(ServerLevel serverLevel, DamageSource damageSource) {
		super.destroy(serverLevel, damageSource);
		if (getBannerItem() != null && !getBannerItem().isEmpty()) {
			spawnAtLocation(serverLevel, getBannerItem());
			setBannerItem(ItemStack.EMPTY);
		}
	}

	@Inject(method = "interact", at = @At(value = "HEAD"), cancellable = true)
	private void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

		if (!MBFOptions.BOAT_BANNERS.getBooleanValue()) {
			return;
		}

		if (player.isSecondaryUseActive()) {
			cir.setReturnValue(InteractionResult.PASS);
			return;
		}

		ItemStack itemStack = player.getItemInHand(hand);
		if (itemStack.getItem() instanceof BannerItem) {
			if (ItemStack.isSameItem(getBannerItem(), itemStack)) return;

			if (!getBannerItem().isEmpty() && this.level() instanceof ServerLevel serverLevel) {
				spawnAtLocation(serverLevel, getBannerItem());
				setBannerItem(ItemStack.EMPTY);
			}

			ItemStack copy = itemStack.copy();
			copy.setCount(1);
			setBannerItem(copy);

			if (!player.getAbilities().instabuild) {
				itemStack.shrink(1);
			}

			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		} else if (itemStack.getItem() instanceof ShearsItem && !getBannerItem().isEmpty() && this.level() instanceof ServerLevel serverLevel) {
			spawnAtLocation(serverLevel, getBannerItem());

			this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
			this.gameEvent(GameEvent.SHEAR, player);
			itemStack.hurtAndBreak(1, player, getSlotForHand(hand));

			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		}

	}

	private static EquipmentSlot getSlotForHand(InteractionHand interactionHand) {
		return interactionHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
	}

}
