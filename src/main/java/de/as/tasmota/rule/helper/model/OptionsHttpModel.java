package de.as.tasmota.rule.helper.model;

import de.as.tasmota.rule.helper.eventbus.ValueRegistry;

public class OptionsHttpModel extends ModelBase<RuleEditorModel> {

    protected final ValueRegistry<String, String> stringValueBus = ValueRegistry.instance();

    @Override
    protected ValueRegistry<String, String> getStringRegistry() {
        return this.stringValueBus;
    }

    public static final String KEY_OPT_IP = "optIP:text";
    public static final String KEY_OPT_USER = "optUser:text";
    public static final String KEY_OPT_PASS = "optPass:text";

    public OptionsHttpModel(RuleEditorModel root) {
        super(root);
    }

    public String getOptIp() {
        return this.getString(KEY_OPT_IP);
    }

    public void setOptIp(String text) {
        this.setString(KEY_OPT_IP, text);
    }

    public String getOptUser() {
        return this.getString(KEY_OPT_USER);
    }

    public void setOptUser(String text) {
        this.setString(KEY_OPT_USER, text);
    }

    public String getOptPass() {
        return this.getString(KEY_OPT_PASS);
    }

    public void setOptPass(String text) {
        this.setString(KEY_OPT_PASS, text);
    }

    public void actionConnect() {
        // TODO
        System.out.println("actionConnect");
    }

}
