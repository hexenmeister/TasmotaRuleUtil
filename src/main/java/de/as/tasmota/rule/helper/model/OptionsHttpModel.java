package de.as.tasmota.rule.helper.model;

public class OptionsHttpModel extends ModelBase {

    public static final String KEY_OPT_IP = "optIP:text";
    public static final String KEY_OPT_USER = "optUser:text";
    public static final String KEY_OPT_PASS = "optPass:text";

    private RuleEditorModel root;

    public OptionsHttpModel(RuleEditorModel root) {
	this.root = root;
    }

    public RuleEditorModel getRoot() {
	return this.root;
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

}
