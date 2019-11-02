package de.as.tasmota.rule.helper.controller;

import de.as.tasmota.rule.helper.model.DevRuleModel;

public class DevRuleController extends ControllerBase<DevRuleModel, RuleEditorController> {

    public DevRuleController(DevRuleModel model, RuleEditorController root) {
	super(model, root);
    }

    public void actionUploadToDevice() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: upload");
    }

    public void actionDownloadFromDevice() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: download");
    }

    public void actionSendToEditor() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: sent to editor");
    }

    public void actionGetFromEditor() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: get form editor");
    }

    
}
