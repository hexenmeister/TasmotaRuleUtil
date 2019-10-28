package de.as.tasmota.rule.helper.controller;

import de.as.tasmota.rule.helper.model.RuleEditorModel;

public class ControllerCreator {

    public static RuleEditorController createController() {
	RuleEditorController controller = new RuleEditorController(new RuleEditorModel());
	return controller;
    }
}
