package de.as.tasmota.rule.helper.controller;

import de.as.tasmota.rule.helper.model.RuleEditorModel;

public class RuleEditorController extends ControllerBase<RuleEditorModel, RuleEditorController> {

    private DevRuleController devRuleController1;
    private DevRuleController devRuleController2;
    private DevRuleController devRuleController3;
    private OptionsHttpController optionsHttpController;

    public RuleEditorController() {
	this(new RuleEditorModel());
    }

    public RuleEditorController(RuleEditorModel model) {
	super(model, null);

	this.devRuleController1 = new DevRuleController(model.getDevRuleModel1(), this);
	this.devRuleController2 = new DevRuleController(model.getDevRuleModel2(), this);
	this.devRuleController3 = new DevRuleController(model.getDevRuleModel3(), this);
	this.optionsHttpController = new OptionsHttpController(model.getOptionsHttpModel(), this);
    }

    public DevRuleController getDevRuleController1() {
	return devRuleController1;
    }

    public DevRuleController getDevRuleController2() {
	return devRuleController2;
    }

    public DevRuleController getDevRuleController3() {
	return devRuleController3;
    }

    public OptionsHttpController getOptionsHttpController() {
	return optionsHttpController;
    }
}
