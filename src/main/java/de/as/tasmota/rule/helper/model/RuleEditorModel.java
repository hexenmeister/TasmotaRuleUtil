package de.as.tasmota.rule.helper.model;

public class RuleEditorModel extends ModelBase {

    public static final String KEY_RULEEDITOR_TEXT = "ruleEditor:text";

    private DevRuleModel devRuleModel1 = new DevRuleModel(this, "Rule1");
    private DevRuleModel devRuleModel2 = new DevRuleModel(this, "Rule2");
    private DevRuleModel devRuleModel3 = new DevRuleModel(this, "Rule3");
    private OptionsHttpModel optionsHttpModel = new OptionsHttpModel(this);

    private RuleEditorModel root;

    public RuleEditorModel() {
	this.root = this;
    }

    public RuleEditorModel getRoot() {
	return this.root;
    }

    public DevRuleModel getDevRuleModel1() {
	return devRuleModel1;
    }

    public DevRuleModel getDevRuleModel2() {
	return devRuleModel2;
    }

    public DevRuleModel getDevRuleModel3() {
	return devRuleModel3;
    }

    public OptionsHttpModel getOptionsHttpModel() {
	return optionsHttpModel;
    }

    public String getEditorText() {
	return this.getString(KEY_RULEEDITOR_TEXT);
    }

    public void setEditorText(String text) {
	this.setString(KEY_RULEEDITOR_TEXT, text);
    }
}
