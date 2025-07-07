package dev.svrt.domiirl.mbf.screen;

import dev.svrt.domiirl.mbf.config.MBFConfigManager;
import dev.svrt.domiirl.mbf.config.options.BooleanOption;
import dev.svrt.domiirl.mbf.config.options.IOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.0
 */
public class MBFOptionsScreen extends OptionsSubScreen {

	private final Screen previous;

	@SuppressWarnings("resource")
	public MBFOptionsScreen(Screen previous) {
		super(previous, Minecraft.getInstance().options, Component.translatable("mbf.options"));
		this.previous = previous;
	}

	@Override
	protected void addOptions() {
		this.list.addSmall(getAllToDisplay());
	}

	@Override
	public void removed() {
		MBFConfigManager.save();
	}

	public static OptionInstance<?>[] getAllToDisplay() {
		return MBFConfigManager.getAllOptions().stream().filter(IOption::shouldDisplay).map(MBFOptionsScreen::toOption).toArray(OptionInstance[]::new);
	}

	public static OptionInstance<?> toOption(IOption option) {
		if (option instanceof BooleanOption bOption) {
			if (bOption.tooltip != null) {
				return OptionInstance.createBoolean("mbf.options." + bOption.key, (value) -> Tooltip.create(bOption.tooltip), bOption.value, newValue -> {
					bOption.value = newValue;
				});
			} else {
				return OptionInstance.createBoolean("mbf.options." + bOption.key, bOption.value, newValue -> {
					bOption.value = newValue;
				});
			}
		}
		return null;
	}

}
