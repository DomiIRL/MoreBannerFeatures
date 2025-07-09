package dev.svrt.domiirl.mbf.mixin.strider;

import dev.svrt.domiirl.mbf.accessor.StriderBannerable;
import dev.svrt.domiirl.mbf.config.MBFOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Strider.class)
public abstract class StriderMixin extends Animal implements StriderBannerable {

	private static final EntityDataAccessor<ItemStack> BANNER = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.ITEM_STACK);

	protected StriderMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @NotNull ItemStack moreBannerFeatures$getBannerItem() {
		if (!MBFOptions.STRIDER_BANNERS.getBooleanValue()) {
			return ItemStack.EMPTY;
		}
		return this.entityData.get(BANNER);
	}

	@Override
	public void moreBannerFeatures$setBannerItem(@NotNull ItemStack itemStack) {
		this.entityData.set(BANNER, itemStack);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BANNER, ItemStack.EMPTY);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		input.read("Banner", ItemStack.CODEC).ifPresent(this::moreBannerFeatures$setBannerItem);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		if (!moreBannerFeatures$getBannerItem().isEmpty()) {
			output.storeNullable("Banner", ItemStack.CODEC, moreBannerFeatures$getBannerItem());
		}
	}

	@Override
	protected void dropEquipment(ServerLevel serverLevel) {
		if (!moreBannerFeatures$getBannerItem().isEmpty()) spawnAtLocation(serverLevel, moreBannerFeatures$getBannerItem());
		super.dropEquipment(serverLevel);
	}

	@Override
	public boolean requiresCustomPersistence() {
		return !moreBannerFeatures$getBannerItem().isEmpty() || super.requiresCustomPersistence();
	}

}
