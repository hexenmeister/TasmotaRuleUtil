package de.as.tasmota.rule.helper.model;

import de.as.tasmota.rule.helper.eventbus.ValueRegistry;

public class DevRuleModel extends ModelBase<RuleEditorModel> {

    protected final ValueRegistry<String, String> stringValueBus = ValueRegistry.instance();

    @Override
    protected ValueRegistry<String, String> getStringRegistry() {
        return stringValueBus;
    }

    public static final String KEY_DEV_RULE_TEXT = "devRulePacked:text";
    public static final String KEY_DEV_INFO_TEXT = "devRuleInfo:text";

    private String label;
    private int index;

    public DevRuleModel(RuleEditorModel root, int index) {
        super(root);
        this.index = index;
        label = "Rule" + index;
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }

    public String getRuleText() {
        return getString(KEY_DEV_RULE_TEXT);
    }

    public void setRuleText(String text) {
        setString(KEY_DEV_RULE_TEXT, text);
    }
}
