package dev.svrt.domiirl.morebannerfeatures.utils;

import dev.svrt.domiirl.morebannerfeatures.MoreBannerFeatures;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.4
 */
public class MBFUtils {

	public static ItemStack getCloakItem(Player player) {
		if (MoreBannerFeatures.isTrinketsInstalled()) {
//			try {
//				Optional<dev.emi.trinkets.api.TrinketComponent> component = dev.emi.trinkets.api.TrinketsApi.getTrinketComponent(player);
//
//				if (component.isPresent()) {
//					return component.get().getInventory().get("chest").get("cape").getItem(0);
//				}
//			} catch (Exception exception) {
//				// TRINKETS IS PROBABLY NOT INSTALLED ON SERVER
//			}

		}
		return player.getInventory().getItem(EquipmentSlot.CHEST.getIndex());
	}

}
