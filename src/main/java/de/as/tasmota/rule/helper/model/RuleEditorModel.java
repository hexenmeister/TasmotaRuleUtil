package de.as.tasmota.rule.helper.model;

import de.as.tasmota.rule.helper.eventbus.ValueRegistry;

public class RuleEditorModel extends ModelBase<RuleEditorModel> {

    protected final ValueRegistry<String, String> stringValueBus = ValueRegistry.instance();

    protected ValueRegistry<String, String> getStringRegistry() {
        return this.stringValueBus;
    }

    public static final String KEY_RULEEDITOR_TEXT = "ruleEditor:text";

    private DevRuleModel devRuleModel1 = new DevRuleModel(this, 1);
    private DevRuleModel devRuleModel2 = new DevRuleModel(this, 2);
    private DevRuleModel devRuleModel3 = new DevRuleModel(this, 3);
    private OptionsHttpModel optionsHttpModel = new OptionsHttpModel(this);

    public RuleEditorModel() {
        super(null);
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
