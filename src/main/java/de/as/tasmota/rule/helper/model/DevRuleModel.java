package de.as.tasmota.rule.helper.model;

import de.as.tasmota.rule.helper.eventbus.ValueRegistry;
import de.as.tasmota.rule.helper.services.TasmotaHttpServices;

public class DevRuleModel extends ModelBase<RuleEditorModel> {

    protected final ValueRegistry<String, String> stringValueBus = ValueRegistry.instance();

    @Override
    protected ValueRegistry<String, String> getStringRegistry() {
        return this.stringValueBus;
    }

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
        return this.label;
    }

    public int getIndex() {
        return this.index;
    }

    public String getRuleText() {
        return this.getString(KEY_DEV_RULE_TEXT);
    }

    public void setRuleText(String text) {
        this.setString(KEY_DEV_RULE_TEXT, text);
    }

    public void actionUploadToDevice() {
        // TODO Auto-generated method stub
        System.out.println("TODO: action performed: upload");
    }

    public void actionDownloadFromDevice() {
        String urlBase = this.getRoot().getOptionsHttpModel().getOptIp();
        String user = this.getRoot().getOptionsHttpModel().getOptUser();
        String pass = this.getRoot().getOptionsHttpModel().getOptPass();

        this.setRuleText(TasmotaHttpServices.getInstance().getTasmotaRuleText(urlBase, user, pass, this.getIndex()));
//        try {
//            TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
//                    new DeviceConnectionParameters(urlBase, user, pass));
//            CommandResult response = tc.excuteCommand("Rule" + this.getIndex());
//
//            if (response.isSuccessful()) {
//                JsonData jd = response.getData().getPath("Rules");
//                if (jd == null) {
//                    this.setRuleText("Problem getting rule text. The answer was " + response.getData());
//                } else {
//                    String ruleText = jd != null ? jd.getValue().toString() : "<empty>";
//                    this.setRuleText(ruleText);
//                }
//            } else {
//                String warning = response.getErrorText();
//                this.setRuleText("Error getting rules:\r\n" + warning);
//            }
//        } catch (InvalideConnectionParametersException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (DeviceAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public void actionSendToEditor() {
        // TODO Auto-generated method stub
        System.out.println("TODO: action performed: sent to editor");
    }

    public void actionGetFromEditor() {
        // TODO Auto-generated method stub
        System.out.println("TODO: action performed: get form editor");
    }
}
