package dev.svrt.domiirl.mbf.utils;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BannerableEntity {

  public static void handleInteract(Entity entity, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    if (!(entity instanceof Bannerable bannerable) || !bannerable.moreBannerFeatures$isEnabled() || player.isSecondaryUseActive()) {
      return;
    }

    if (entity instanceof AgeableMob ageableMob && ageableMob.isBaby()) {
      return;
    }

    ItemStack itemStack = player.getItemInHand(hand);
    if (itemStack.getItem() instanceof BannerItem) {
      if (ItemStack.isSameItem(bannerable.moreBannerFeatures$getBannerItem(), itemStack)) return;

      entity.level().playSound(null, entity, SoundEvents.HORSE_STEP_WOOD, SoundSource.PLAYERS, 1.0F, 1.0F);

      if (!bannerable.moreBannerFeatures$getBannerItem().isEmpty()) {
        if (entity.level() instanceof ServerLevel serverLevel) {
          Vec3 vec3 = entity.getType().getDimensions().attachments().getAverage(EntityAttachment.PASSENGER);
          entity.spawnAtLocation(serverLevel, bannerable.moreBannerFeatures$getBannerItem(), vec3);
        }
      }

      ItemStack copy = itemStack.copy();
      copy.setCount(1);
      bannerable.moreBannerFeatures$setBannerItem(copy);

      if (!player.getAbilities().instabuild) {
        itemStack.shrink(1);
      }

      cir.setReturnValue(InteractionResult.SUCCESS);
      cir.cancel();
    } else if (itemStack.getItem() instanceof ShearsItem && !bannerable.moreBannerFeatures$getBannerItem().isEmpty()) {

      entity.level().playSound(null, entity, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
      entity.gameEvent(GameEvent.SHEAR, player);
      itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

      if (entity.level() instanceof ServerLevel serverLevel) {
        Vec3 vec3 = entity.getType().getDimensions().attachments().getAverage(EntityAttachment.PASSENGER);
        entity.spawnAtLocation(serverLevel, bannerable.moreBannerFeatures$getBannerItem(), vec3);
      }
      bannerable.moreBannerFeatures$setBannerItem(ItemStack.EMPTY);
      cir.setReturnValue(InteractionResult.SUCCESS);
      cir.cancel();
    }
  }

}
