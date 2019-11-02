package de.as.tasmota.rule.helper.controller;

import java.util.List;

import de.as.tasmota.rule.helper.logic.formatter.RulePacker;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser.Part;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser.RuleBlock;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser.RuleScript;
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

    public void actionFormatCode() {
	RuleScript script = RuleParser.parse(getModel().getEditorText());
	getModel().setEditorText(script.writeFormated() + script.getUnparsedText());
    }

    public void actionPackCode() {
	String text = getModel().getEditorText();
	RuleScript script = RuleParser.parse(text);
	String r1 = "";
	String r2 = "";
	String r3 = "";
	boolean found = false;
	for (Part part : script.getChildren()) {
	    if (part instanceof RuleBlock) {
		found = true;
		if (((RuleBlock) part).getName().equalsIgnoreCase("rule1")) {
		    r1 = ((RuleBlock) part).write(false);
		}
		if (((RuleBlock) part).getName().equalsIgnoreCase("rule2")) {
		    r2 = ((RuleBlock) part).write(false);
		}
		if (((RuleBlock) part).getName().equalsIgnoreCase("rule3")) {
		    r3 = ((RuleBlock) part).write(false);
		}
	    }
	}

	if (!found) {
	    r1 = script.write();
	}

	RulePacker packer = new RulePacker();
	List<String> rpacked1 = packer.pack(r1);
	List<String> rpacked2 = packer.pack(r2);
	List<String> rpacked3 = packer.pack(r3);
	// TODO
	getModel().getDevRuleModel1().setRuleText(rpacked1.get(0));
	getModel().getDevRuleModel2().setRuleText(rpacked2.get(0));
	getModel().getDevRuleModel3().setRuleText(rpacked3.get(0));
    }

    public void actionLoad() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: load");
    }

    public void actionSave() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: save");
    }
}
