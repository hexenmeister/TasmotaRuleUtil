package de.as.tasmota.rule.helper.eventbus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EventRegistry<KEY, VALUE> {
    private final Map<KEY, List<Consumer<VALUE>>> listeners = new ConcurrentHashMap<>();

    public static <K, V> EventRegistry<K, V> instance() {
	return new EventRegistry<>();
    }

    public Registration register(KEY key, Consumer<VALUE> listener) {
	List<Consumer<VALUE>> consumers = this.listeners.get(key);
	if (consumers == null) {
	    consumers = new CopyOnWriteArrayList<>();
	    this.listeners.put(key, consumers);
	}
	consumers.add(listener);
	final List<Consumer<VALUE>> fc = consumers;
	return () -> fc.remove(listener);
    }

    public void sendEvent(KEY key, VALUE event) {
////	Stream.ofNullable(this.listeners.get(key)).forEach((listener) -> listener.accept(event));
//	Stream.ofNullable(this.listeners.get(key)).forEach(System.out::println);
//	this.listeners.get(key).forEach(listener -> listener.accept(event));
	List<Consumer<VALUE>> list = this.listeners.get(key);
	if (list != null) {
	    list.forEach(listener -> listener.accept(event));
	}
    }

//    public void sendEvent(VALUE event) {
//	this.listeners.values().forEach(consumers -> consumers.forEach(listener -> listener.accept(event)));
//    }
}
