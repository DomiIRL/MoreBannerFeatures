package dev.svrt.domiirl.mbf.config.options;

import com.google.gson.JsonObject;

public interface IOption {
	String getKey();
	void write(JsonObject config);
	void read(JsonObject config);
	boolean shouldDisplay();
}
