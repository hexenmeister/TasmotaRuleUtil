package de.as.tasmota.rule.helper.controller;

import de.as.tasmota.rule.helper.model.ModelBase;

public class ControllerBase<M extends ModelBase<?>, C extends ControllerBase<?, ?>> {

    private M model;
    private C root;

    protected ControllerBase(M model) {
	this(model, null);
    }

    public ControllerBase(M model, C root) {
	this.model = model;
	this.root = root;
    }

    public M getModel() {
	return this.model;
    }

    @SuppressWarnings("unchecked")
    public C getRoot() {
	if (this.root == null) {
	    return (C) this;
	}
	return this.root;
    }
}
