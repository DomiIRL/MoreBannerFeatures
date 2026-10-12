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
//? if >=1.21.6 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import dev.svrt.domiirl.mbf.utils.BannerSaveData;
import net.minecraft.nbt.CompoundTag;
*///?}
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Strider.class)
public abstract class StriderMixin extends Animal implements StriderBannerable {

	@Unique
	private static final EntityDataAccessor<ItemStack> BANNER = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.ITEM_STACK);

	protected StriderMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @NotNull ItemStack mbf$getBannerItem() {
		if (!MBFOptions.STRIDER_BANNERS.getBooleanValue()) {
			return ItemStack.EMPTY;
		}
		return this.entityData.get(BANNER);
	}

	@Override
	public void mbf$setBannerItem(@NotNull ItemStack itemStack) {
		this.entityData.set(BANNER, itemStack);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BANNER, ItemStack.EMPTY);
	}

	//? if >=1.21.6 {
	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		input.read("Banner", ItemStack.CODEC).ifPresent(this::mbf$setBannerItem);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		if (!mbf$getBannerItem().isEmpty()) {
			output.storeNullable("Banner", ItemStack.CODEC, mbf$getBannerItem());
		}
	}
	//?} else {
	/*@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		BannerSaveData.read(tag, this.registryAccess()).ifPresent(this::mbf$setBannerItem);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		BannerSaveData.write(tag, this.registryAccess(), mbf$getBannerItem());
	}
	*///?}

	@Override
	public void dropEquipment(ServerLevel serverLevel) {
		if (!mbf$getBannerItem().isEmpty()) spawnAtLocation(serverLevel, mbf$getBannerItem());
		super.dropEquipment(serverLevel);
	}

	@Override
	public boolean requiresCustomPersistence() {
		return !mbf$getBannerItem().isEmpty() || super.requiresCustomPersistence();
	}

}
