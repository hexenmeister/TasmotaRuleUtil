package de.as.tasmota.rule.helper.eventbus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ValueRegistry<KEY, VALUE> {
    private Map<KEY, Supplier<VALUE>> getterMap = new HashMap<>();
    private Map<KEY, Consumer<VALUE>> setterMap = new HashMap<>();

    public static <K, V> ValueRegistry<K, V> instance() {
	return new ValueRegistry<>();
    }

    public Registration register(KEY key, Supplier<VALUE> getter, Consumer<VALUE> setter) {
	if (getter != null) {
	    this.getterMap.put(key, getter);
	}

	if (setter != null) {
	    this.setterMap.put(key, setter);
	}

	return () -> {
	    this.getterMap.remove(key);
	    this.setterMap.remove(key);
	};
    }

    public VALUE get(KEY key) {
	Supplier<VALUE> supplier = this.getterMap.get(key);
	if (supplier != null) {
	    return supplier.get();
	}
	throw new UnsupportedOperationException("no suplier defined for key: " + key);
    }

    public void set(KEY key, VALUE value) {
	Consumer<VALUE> consumer = this.setterMap.get(key);
	if (consumer != null) {
	    consumer.accept(value);
	} else {
	    throw new UnsupportedOperationException("no consumer defined for key: " + key);
	}
    }
}
