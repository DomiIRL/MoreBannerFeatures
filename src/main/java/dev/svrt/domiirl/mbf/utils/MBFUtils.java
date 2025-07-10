package dev.svrt.domiirl.mbf.utils;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.Map;

public class MBFUtils {

	// Trinkets reflection
	private static Class<?> trinketsApiClass;
	private static Method getTrinketComponentMethod;
	private static Method isPresentMethod;
	private static Method getMethod;
	private static Method getInventoryMethod;
	private static Method getItemMethod;
	private static boolean trinketsReflectionInitialized = false;
	private static boolean trinketsReflectionFailed = false;

	// Accessories reflection
	private static Class<?> accessoriesCapabilityClass;
	private static Method getAccessoriesCapabilityMethod;
	private static Method getContainersMethod;
	private static Class<?> accessoriesContainerClass;
	private static Method getAccessoriesMethod;
	private static Method getItemMethodAccessories;
	private static boolean accessoriesReflectionInitialized = false;
	private static boolean accessoriesReflectionFailed = false;

	private static void initTrinketsReflection() {
		if (trinketsReflectionInitialized || trinketsReflectionFailed) return;
		try {
			trinketsApiClass = Class.forName("dev.emi.trinkets.api.TrinketsApi");
			getTrinketComponentMethod = trinketsApiClass.getMethod("getTrinketComponent", LivingEntity.class);

			Class<?> optionalClass = Class.forName("java.util.Optional");
			isPresentMethod = optionalClass.getMethod("isPresent");
			getMethod = optionalClass.getMethod("get");

			Class<?> trinketComponentClass = Class.forName("dev.emi.trinkets.api.TrinketComponent");
			getInventoryMethod = trinketComponentClass.getMethod("getInventory");

			Class<?> trinketInventoryClass = Class.forName("dev.emi.trinkets.api.TrinketInventory");
			getItemMethod = trinketInventoryClass.getMethod("getItem", int.class);

			trinketsReflectionInitialized = true;
		} catch (Exception e) {
			trinketsReflectionFailed = true;
		}
	}

	private static void initAccessoriesReflection() {
		if (accessoriesReflectionInitialized || accessoriesReflectionFailed) return;
		try {
			accessoriesCapabilityClass = Class.forName("io.wispforest.accessories.api.AccessoriesCapability");
			getAccessoriesCapabilityMethod = accessoriesCapabilityClass.getMethod("get", LivingEntity.class);
			getContainersMethod = accessoriesCapabilityClass.getMethod("getContainers");

			accessoriesContainerClass = Class.forName("io.wispforest.accessories.api.AccessoriesContainer");
			getAccessoriesMethod = accessoriesContainerClass.getMethod("getAccessories");

			Class<?> accessoriesInventoryClass = Class.forName("io.wispforest.accessories.api.AccessoriesInventory");
			getItemMethodAccessories = accessoriesInventoryClass.getMethod("getItem", int.class);

			accessoriesReflectionInitialized = true;
		} catch (Exception e) {
			accessoriesReflectionFailed = true;
		}
	}

	@SuppressWarnings("unchecked")
	public static ItemStack getTrinketSlotItem(LivingEntity entity, String equipmentSlot, String slotName) {
		initTrinketsReflection();
		if (trinketsReflectionInitialized) {
			try {
				Object optionalComponent = getTrinketComponentMethod.invoke(null, entity);
				if ((boolean) isPresentMethod.invoke(optionalComponent)) {
					Object component = getMethod.invoke(optionalComponent);
					Object inventory = getInventoryMethod.invoke(component);
					Object slotMap = ((Map<?, ?>) inventory).get(equipmentSlot);
					if (slotMap != null) {
						Object trinketInventory = ((Map<?, ?>) slotMap).get(slotName);
						if (trinketInventory != null) {
							ItemStack item = (ItemStack) getItemMethod.invoke(trinketInventory, 0);
							if (!item.isEmpty()) {
								return item;
							}
						}
					}
				}
			} catch (Exception ignored) {
			}
		}
		return ItemStack.EMPTY;
	}

	@SuppressWarnings("unchecked")
	public static ItemStack getAccessoriesItem(LivingEntity entity, String slotName) {
		if (!MoreBannerFeatures.isAccessoriesInstalled()) return ItemStack.EMPTY;
		initAccessoriesReflection();
		if (accessoriesReflectionInitialized) {
			try {
				Object capability = getAccessoriesCapabilityMethod.invoke(null, entity);
				if (capability == null) return ItemStack.EMPTY;
				Map<String, Object> containers = (Map<String, Object>) getContainersMethod.invoke(capability);
				if (containers == null || containers.isEmpty()) return ItemStack.EMPTY;
				Object container = containers.get(slotName);
				if (container == null) return ItemStack.EMPTY;
				Object accessoriesInventory = getAccessoriesMethod.invoke(container);
				if (accessoriesInventory == null) return ItemStack.EMPTY;
				ItemStack stack = (ItemStack) getItemMethodAccessories.invoke(accessoriesInventory, 0);
				return stack == null ? ItemStack.EMPTY : stack;
			} catch (Exception ignored) {
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getCloakItem(LivingEntity entity) {
		ItemStack trinketCape = getTrinketSlotItem(entity, "chest", "cape");
		System.out.println(trinketCape);
		if (!trinketCape.isEmpty()) {
			return trinketCape;
		}
		ItemStack accessoriesCape = getAccessoriesItem(entity, "cape");
		if (!accessoriesCape.isEmpty()) {
			return accessoriesCape;
		}
		return entity.getItemBySlot(EquipmentSlot.CHEST);
	}

	public static ItemStack getHeadItem(LivingEntity entity) {
		ItemStack trinketsBanner = getTrinketSlotItem(entity, "head", "hat");
		if (!trinketsBanner.isEmpty()) {
			return trinketsBanner;
		}
		ItemStack accessoriesBanner = getAccessoriesItem(entity, "hat");
		if (!accessoriesBanner.isEmpty()) {
			return accessoriesBanner;
		}
		return entity.getItemBySlot(EquipmentSlot.HEAD);
	}
}
