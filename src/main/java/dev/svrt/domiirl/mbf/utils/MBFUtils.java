package dev.svrt.domiirl.mbf.utils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MBFUtils {

	public static ItemStack getCloakItem(Player player) {
//		Trinkets doesnt support 1.21.6/7 yet
//		if (MoreBannerFeatures.isTrinketsInstalled()) {
//			try {
//				Optional<TrinketComponent> component = dev.emi.trinkets.api.TrinketsApi.getTrinketComponent(player);
//				if (component.isPresent()) {
//					return component.get().getInventory().get("chest").get("cape").getComponent(0);
//				}
//			} catch (Exception exception) {
//				// TRINKETS IS PROBABLY NOT INSTALLED ON SERVER
//			}
//		}
		return player.getItemBySlot(EquipmentSlot.CHEST);
	}

}
