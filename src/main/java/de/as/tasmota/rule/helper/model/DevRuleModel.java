package de.as.tasmota.rule.helper.model;

public class DevRuleModel extends ModelBase {

    public static final String KEY_DEV_RULE_TEXT = "devRule:text";

    private RuleEditorModel root;
    private String label;

    public DevRuleModel(RuleEditorModel root, String label) {
	this.root = root;
	this.label = label;
    }

    public RuleEditorModel getRoot() {
	return this.root;
    }

    public String getLabel() {
	return label;
    }

    public DevRuleModel(String label) {
	this.label = label;
    }

    public String getRuleText() {
	return this.getString(KEY_DEV_RULE_TEXT);
    }

    public void setRuleText(String text) {
	this.setString(KEY_DEV_RULE_TEXT, text);
    }
}
