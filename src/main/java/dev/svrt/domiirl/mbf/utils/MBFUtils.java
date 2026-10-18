package dev.svrt.domiirl.mbf.utils;

import dev.svrt.domiirl.mbf.MoreBannerFeatures;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// The slot mods' inventories are all Minecraft Containers. Their items are read through that type, never by
// reflecting on getItem: Minecraft's method names are obfuscated outside the dev environment.
public class MBFUtils {

	// Trinkets reflection
	private static Class<?> trinketsApiClass;
	private static Method getTrinketComponentMethod;
	private static Method isPresentMethod;
	private static Method getMethod;
	private static Method getInventoryMethod;
	private static boolean trinketsReflectionInitialized = false;
	private static boolean trinketsReflectionFailed = false;

	// Trinkets Updated reflection
	private static Method getAttachmentMethod;
	private static Method getInventoryBySlotIdMethod;
	private static boolean trinketsUpdatedReflectionInitialized = false;
	private static boolean trinketsUpdatedReflectionFailed = false;

	// Accessories reflection
	private static Class<?> accessoriesCapabilityClass;
	private static Method getAccessoriesCapabilityMethod;
	private static Method getContainersMethod;
	private static Class<?> accessoriesContainerClass;
	private static Method getAccessoriesMethod;
	private static Method shouldRenderAccessoryMethod;
	private static boolean accessoriesReflectionInitialized = false;
	private static boolean accessoriesReflectionFailed = false;

	// Curios reflection
	private static Object curiosLookupTarget;
	private static Method getCuriosInventoryMethod;
	private static Method resolveLazyOptionalMethod;
	private static Method getStacksHandlerMethod;
	private static Method getStacksMethod;
	private static Method getRendersMethod;
	private static Method getStackInSlotMethod;
	private static boolean curiosReflectionInitialized = false;
	private static boolean curiosReflectionFailed = false;

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
			try {
				shouldRenderAccessoryMethod = accessoriesContainerClass.getMethod("shouldRender", int.class);
			} catch (NoSuchMethodException e) {
				// Older Accessories releases have no per-slot visibility toggle
			}

			accessoriesReflectionInitialized = true;
		} catch (Exception e) {
			accessoriesReflectionFailed = true;
		}
	}

	private static void initCuriosReflection() {
		if (curiosReflectionInitialized || curiosReflectionFailed) return;
		try {
			Class<?> apiClass = Class.forName("top.theillusivec4.curios.api.CuriosApi");
			try {
				getCuriosInventoryMethod = apiClass.getMethod("getCuriosInventory", LivingEntity.class);
			} catch (NoSuchMethodException e) {
				// Curios before 1.20 reaches the inventory through its helper
				curiosLookupTarget = apiClass.getMethod("getCuriosHelper").invoke(null);
				getCuriosInventoryMethod = Class.forName("top.theillusivec4.curios.api.type.util.ICuriosHelper").getMethod("getCuriosHandler", LivingEntity.class);
			}
			// Forge hands out a LazyOptional, NeoForge a plain Optional
			if (getCuriosInventoryMethod.getReturnType() != Optional.class) {
				resolveLazyOptionalMethod = getCuriosInventoryMethod.getReturnType().getMethod("resolve");
			}

			Class<?> handlerClass = Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler");
			getStacksHandlerMethod = handlerClass.getMethod("getStacksHandler", String.class);

			Class<?> stacksHandlerClass = Class.forName("top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler");
			getStacksMethod = stacksHandlerClass.getMethod("getStacks");
			getRendersMethod = stacksHandlerClass.getMethod("getRenders");

			Class<?> stackHandlerClass = Class.forName("top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler");
			getStackInSlotMethod = stackHandlerClass.getMethod("getStackInSlot", int.class);

			curiosReflectionInitialized = true;
		} catch (Exception e) {
			curiosReflectionFailed = true;
		}
	}

	private static void initTrinketsUpdatedReflection() {
		if (trinketsUpdatedReflectionInitialized || trinketsUpdatedReflectionFailed) return;
		try {
			Class<?> apiClass = Class.forName("eu.pb4.trinkets.api.TrinketsApi");
			getAttachmentMethod = apiClass.getMethod("getAttachment", LivingEntity.class);

			Class<?> attachmentClass = Class.forName("eu.pb4.trinkets.api.TrinketAttachment");
			getInventoryBySlotIdMethod = attachmentClass.getMethod("getInventory", String.class);

			trinketsUpdatedReflectionInitialized = true;
		} catch (Exception e) {
			trinketsUpdatedReflectionFailed = true;
		}
	}

	// slotId is "<group>/<slot>"
	public static ItemStack getTrinketsUpdatedSlotItem(LivingEntity entity, String slotId) {
		if (!MoreBannerFeatures.isTrinketsInstalled()) return ItemStack.EMPTY;
		initTrinketsUpdatedReflection();
		if (trinketsUpdatedReflectionInitialized) {
			try {
				Object attachment = getAttachmentMethod.invoke(null, entity);
				if (attachment == null) return ItemStack.EMPTY;
				Object inventory = getInventoryBySlotIdMethod.invoke(attachment, slotId);
				if (inventory == null) return ItemStack.EMPTY;
				ItemStack stack = ((Container) inventory).getItem(0);
				return stack == null ? ItemStack.EMPTY : stack;
			} catch (Exception ignored) {
			}
		}
		return ItemStack.EMPTY;
	}

	@SuppressWarnings("unchecked")
	public static ItemStack getTrinketSlotItem(LivingEntity entity, String equipmentSlot, String slotName) {
		if (!MoreBannerFeatures.isTrinketsInstalled()) return ItemStack.EMPTY;
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
							ItemStack item = ((Container) trinketInventory).getItem(0);
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
				// The player hid this slot with its visibility toggle
				if (shouldRenderAccessoryMethod != null && !(boolean) shouldRenderAccessoryMethod.invoke(container, 0)) return ItemStack.EMPTY;
				Object accessoriesInventory = getAccessoriesMethod.invoke(container);
				if (accessoriesInventory == null) return ItemStack.EMPTY;
				ItemStack stack = ((Container) accessoriesInventory).getItem(0);
				return stack == null ? ItemStack.EMPTY : stack;
			} catch (Exception ignored) {
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getCuriosItem(LivingEntity entity, String slotId) {
		if (!MoreBannerFeatures.isCuriosInstalled()) return ItemStack.EMPTY;
		initCuriosReflection();
		if (curiosReflectionInitialized) {
			try {
				Object inventory = getCuriosInventoryMethod.invoke(curiosLookupTarget, entity);
				Optional<?> handler = (Optional<?>) (resolveLazyOptionalMethod == null ? inventory : resolveLazyOptionalMethod.invoke(inventory));
				if (handler.isEmpty()) return ItemStack.EMPTY;
				Optional<?> stacksHandler = (Optional<?>) getStacksHandlerMethod.invoke(handler.get(), slotId);
				if (stacksHandler.isEmpty()) return ItemStack.EMPTY;
				// The player hid this slot with its visibility toggle
				List<?> renders = (List<?>) getRendersMethod.invoke(stacksHandler.get());
				if (!renders.isEmpty() && Boolean.FALSE.equals(renders.get(0))) return ItemStack.EMPTY;
				ItemStack stack = (ItemStack) getStackInSlotMethod.invoke(getStacksMethod.invoke(stacksHandler.get()), 0);
				return stack == null ? ItemStack.EMPTY : stack;
			} catch (Exception ignored) {
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getCloakItem(LivingEntity entity) {
		ItemStack trinketCape = getTrinketSlotItem(entity, "chest", "cape");
		if (!trinketCape.isEmpty()) {
			return trinketCape;
		}
		ItemStack trinketsUpdatedCape = getTrinketsUpdatedSlotItem(entity, "chest/cape");
		if (!trinketsUpdatedCape.isEmpty()) {
			return trinketsUpdatedCape;
		}
		ItemStack accessoriesCape = getAccessoriesItem(entity, "cape");
		if (!accessoriesCape.isEmpty()) {
			return accessoriesCape;
		}
		// Curios has no cape slot, capes go on the back
		ItemStack curiosCape = getCuriosItem(entity, "back");
		if (!curiosCape.isEmpty()) {
			return curiosCape;
		}
		return entity.getItemBySlot(EquipmentSlot.CHEST);
	}

	// Not consulted for the head, Trinkets Updated renders that itself
	public static ItemStack getHeadItem(LivingEntity entity) {
		ItemStack trinketsBanner = getTrinketSlotItem(entity, "head", "hat");
		if (!trinketsBanner.isEmpty()) {
			return trinketsBanner;
		}
		ItemStack accessoriesBanner = getAccessoriesItem(entity, "hat");
		if (!accessoriesBanner.isEmpty()) {
			return accessoriesBanner;
		}
		ItemStack curiosBanner = getCuriosItem(entity, "head");
		if (!curiosBanner.isEmpty()) {
			return curiosBanner;
		}
		return entity.getItemBySlot(EquipmentSlot.HEAD);
	}
}
