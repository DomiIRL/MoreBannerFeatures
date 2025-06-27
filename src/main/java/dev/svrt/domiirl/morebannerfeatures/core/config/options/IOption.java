package dev.svrt.domiirl.morebannerfeatures.core.config.options;

import com.google.gson.JsonObject;

/**
 * @author KxmischesDomi | https://github.com/domiirl
 * @since 1.1.0
 */
public interface IOption {
	String getKey();
	void write(JsonObject config);
	void read(JsonObject config);
	boolean shouldDisplay();
}
