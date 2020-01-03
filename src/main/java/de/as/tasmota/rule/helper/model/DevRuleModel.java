package de.as.tasmota.rule.helper.model;

public class DevRuleModel extends ModelBase<RuleEditorModel> {

    public static final String KEY_DEV_RULE_TEXT = "devRulePacked:text";
    public static final String KEY_DEV_INFO_TEXT = "devRuleInfo:text";

    private String label;
    private int index;

    public DevRuleModel(RuleEditorModel root, int index) {
	super(root);
	this.index = index;
	this.label = "Rule" + index;
    }

    public String getLabel() {
	return label;
    }

    public int getIndex() {
	return index;
    }

    public String getRuleText() {
	return this.getString(KEY_DEV_RULE_TEXT);
    }

    public void setRuleText(String text) {
	this.setString(KEY_DEV_RULE_TEXT, text);
    }
}
