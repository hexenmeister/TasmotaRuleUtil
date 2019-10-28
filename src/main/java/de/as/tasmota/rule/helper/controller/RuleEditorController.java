package de.as.tasmota.rule.helper.controller;

import de.as.tasmota.rule.helper.model.RuleEditorModel;

public class RuleEditorController extends ControllerBase<RuleEditorModel, RuleEditorController> {

    private DevRuleController devRuleModel1;
    private DevRuleController devRuleModel2;
    private DevRuleController devRuleModel3;
    private OptionsHttpController optionsHttpModel;

    public RuleEditorController() {
	this(new RuleEditorModel());
    }

    public RuleEditorController(RuleEditorModel model) {
	super(model, null);

	this.devRuleModel1 = new DevRuleController(model.getDevRuleModel1(), this);
	this.devRuleModel2 = new DevRuleController(model.getDevRuleModel2(), this);
	this.devRuleModel3 = new DevRuleController(model.getDevRuleModel3(), this);
	this.optionsHttpModel = new OptionsHttpController(model.getOptionsHttpModel(), this);
    }

    public DevRuleController getDevRuleModel1() {
	return devRuleModel1;
    }

    public DevRuleController getDevRuleModel2() {
	return devRuleModel2;
    }

    public DevRuleController getDevRuleModel3() {
	return devRuleModel3;
    }

    public OptionsHttpController getOptionsHttpModel() {
	return optionsHttpModel;
    }
}
