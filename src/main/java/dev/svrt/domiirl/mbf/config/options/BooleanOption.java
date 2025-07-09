package dev.svrt.domiirl.mbf.config.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.chat.Component;

public class BooleanOption implements IOption {

	public final String key;

	public boolean value;
	private boolean defaultValue;
	public Component tooltip;
	private boolean display = true;

	public BooleanOption(String key) {
		this.key = key;
		this.value = false;
		this.tooltip = Component.translatable("mbf.tooltip." + key);
	}

	public BooleanOption(String key, boolean defaultValue) {
		this.key = key;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.tooltip = Component.translatable("mbf.tooltip." + key);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void write(JsonObject config) {
		config.addProperty(key, value);
	}

	@Override
	public void read(JsonObject config) {
		JsonPrimitive member = config.getAsJsonPrimitive(key);
		value = member == null ? defaultValue : member.getAsBoolean();
	}

	@Override
	public boolean shouldDisplay() {
		return display;
	}

	public boolean getBooleanValue() {
		return value;
	}

	public BooleanOption display(boolean display) {
		this.display = display;
		return this;
	}

	public BooleanOption tooltip(Component tooltip) {
		this.tooltip = tooltip;
		return this;
	}

}
