package de.as.tasmota.rule.helper.model;

import java.util.HashMap;
import java.util.Map;

public class ModelBase {

    private Map<String, ValueBridge<?>> map = new HashMap<>();

    public static abstract class ValueBridge<T> {
	public abstract T getValue();

	public abstract void setValue(T value);
    }

    public void addBridge(String key, ValueBridge<?> bridge) {
	this.map.put(key, bridge);
    }

    public String getString(String key) {
	return (String) this.map.get(key).getValue();
    }

    @SuppressWarnings("unchecked")
    public void setString(String key, String value) {
	((ValueBridge<String>) this.map.get(key)).setValue(value);
    }

}
