package dev.svrt.domiirl.mbf.utils;

import dev.svrt.domiirl.mbf.accessor.Bannerable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

public class BannerableEntity {

  public static final List<EntityType<?>> BANNERABLE_ENTITY_TYPES = new ArrayList<>(List.of(
    EntityType.ACACIA_BOAT,
    EntityType.BIRCH_BOAT,
    EntityType.DARK_OAK_BOAT,
    EntityType.JUNGLE_BOAT,
    EntityType.OAK_BOAT,
    EntityType.SPRUCE_BOAT,
    EntityType.CHERRY_BOAT,
    EntityType.ACACIA_CHEST_BOAT,
    EntityType.BIRCH_CHEST_BOAT,
    EntityType.DARK_OAK_CHEST_BOAT,
    EntityType.JUNGLE_CHEST_BOAT,
    EntityType.OAK_CHEST_BOAT,
    EntityType.SPRUCE_CHEST_BOAT,
    EntityType.CHERRY_CHEST_BOAT,
    EntityType.STRIDER,
    EntityType.HORSE,
    EntityType.DONKEY,
    EntityType.MULE,
    EntityType.SKELETON_HORSE,
    EntityType.ZOMBIE_HORSE,
    EntityType.MINECART,
    EntityType.CHEST_MINECART,
    EntityType.TNT_MINECART,
    EntityType.SPAWNER_MINECART,
    EntityType.FURNACE_MINECART,
    EntityType.COMMAND_BLOCK_MINECART,
    EntityType.HOPPER_MINECART,
    EntityType.HAPPY_GHAST,
    EntityType.VILLAGER
  ));

  public static void handleInteract(Entity entity, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    if (!(BANNERABLE_ENTITY_TYPES.contains(entity.getType()))) {
      return;
    }

    if (!(entity instanceof Bannerable bannerable) || !bannerable.mbf$isEnabled() || player.isSecondaryUseActive()) {
      return;
    }

    if (entity instanceof AgeableMob ageableMob && ageableMob.isBaby()) {
      return;
    }

    if (entity instanceof TamableAnimal tamableAnimal && !tamableAnimal.isTame()) {
      return;
    }

    ItemStack itemStack = player.getItemInHand(hand);
    if (itemStack.getItem() instanceof BannerItem) {
      if (ItemStack.isSameItem(bannerable.mbf$getBannerItem(), itemStack)) return;

      entity.level().playSound(null, entity, SoundEvents.HORSE_STEP_WOOD, SoundSource.PLAYERS, 1.0F, 1.0F);

      if (!bannerable.mbf$getBannerItem().isEmpty()) {
        if (entity.level() instanceof ServerLevel serverLevel) {
          Vec3 vec3 = entity.getType().getDimensions().attachments().getAverage(EntityAttachment.PASSENGER);
          entity.spawnAtLocation(serverLevel, bannerable.mbf$getBannerItem(), vec3);
        }
      }

      ItemStack copy = itemStack.copy();
      copy.setCount(1);
      bannerable.mbf$setBannerItem(copy);

      if (!player.getAbilities().instabuild) {
        itemStack.shrink(1);
      }

      cir.setReturnValue(InteractionResult.SUCCESS);
      cir.cancel();
    } else if (itemStack.getItem() instanceof ShearsItem && !bannerable.mbf$getBannerItem().isEmpty()) {

      entity.level().playSound(null, entity, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
      entity.gameEvent(GameEvent.SHEAR, player);
      EquipmentSlot equipmentSlot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
      itemStack.hurtAndBreak(1, player, equipmentSlot);

      if (entity.level() instanceof ServerLevel serverLevel) {
        Vec3 vec3 = entity.getType().getDimensions().attachments().getAverage(EntityAttachment.PASSENGER);
        entity.spawnAtLocation(serverLevel, bannerable.mbf$getBannerItem(), vec3);
      }
      bannerable.mbf$setBannerItem(ItemStack.EMPTY);
      cir.setReturnValue(InteractionResult.SUCCESS);
      cir.cancel();
    }
  }
}
