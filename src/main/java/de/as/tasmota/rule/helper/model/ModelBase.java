package de.as.tasmota.rule.helper.model;

import java.util.function.Consumer;
import java.util.function.Supplier;

import de.as.tasmota.rule.helper.eventbus.Registration;
import de.as.tasmota.rule.helper.eventbus.ValueRegistry;

public abstract class ModelBase<M extends ModelBase<?>> {

    private M root;

    protected abstract ValueRegistry<String, String> getStringRegistry();

    protected ModelBase(M root) {
        this.root = root;
    }

    @SuppressWarnings("unchecked")
    public M getRoot() {
        if (this.root == null) {
            return (M) this;
        }
        return this.root;
    }

    public Registration registerStringBridge(String key, Supplier<String> getter, Consumer<String> setter) {
        return this.getStringRegistry().register(key, getter, setter);
    }

    public String getString(String key) {
        return this.getStringRegistry().get(key);
    }

    public void setString(String key, String value) {
        this.getStringRegistry().set(key, value);
    }
}
