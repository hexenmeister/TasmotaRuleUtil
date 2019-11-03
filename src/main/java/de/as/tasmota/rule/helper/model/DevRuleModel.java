package de.as.tasmota.rule.helper.model;

public class DevRuleModel extends ModelBase<RuleEditorModel> {

    public static final String KEY_DEV_RULE_TEXT = "devRulePacked:text";
    public static final String KEY_DEV_INFO_TEXT = "devRuleInfo:text";

    private String label;

    public DevRuleModel(RuleEditorModel root, int num) {
	super(root);
	this.label = "Rule" + num;
    }

    public String getLabel() {
	return label;
    }

    public String getRuleText() {
	return this.getString(KEY_DEV_RULE_TEXT);
    }

    public void setRuleText(String text) {
	this.setString(KEY_DEV_RULE_TEXT, text);
    }
}
