package de.as.tasmota.rule.helper.model;

import java.util.List;

import de.as.tasmota.rule.helper.eventbus.ValueRegistry;
import de.as.tasmota.rule.helper.logic.formatter.RulePacker;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser.Part;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser.RuleBlock;
import de.as.tasmota.rule.helper.logic.formatter.RuleParser.RuleScript;

public class RuleEditorModel extends ModelBase<RuleEditorModel> {

    protected final ValueRegistry<String, String> stringValueBus = ValueRegistry.instance();

    @Override
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
        return this.devRuleModel1;
    }

    public DevRuleModel getDevRuleModel2() {
        return this.devRuleModel2;
    }

    public DevRuleModel getDevRuleModel3() {
        return this.devRuleModel3;
    }

    public OptionsHttpModel getOptionsHttpModel() {
        return this.optionsHttpModel;
    }

    public String getEditorText() {
        return this.getString(KEY_RULEEDITOR_TEXT);
    }

    public void setEditorText(String text) {
        this.setString(KEY_RULEEDITOR_TEXT, text);
    }

    public void actionFormatCode() {
        RuleScript script = RuleParser.parse(this.getEditorText());
        this.setEditorText(script.writeFormated() + script.getUnparsedText());
    }

    public void actionPackCode() {
        String text = this.getEditorText();
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
        this.getDevRuleModel1().setRuleText(rpacked1.get(0));
        this.getDevRuleModel2().setRuleText(rpacked2.get(0));
        this.getDevRuleModel3().setRuleText(rpacked3.get(0));
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
