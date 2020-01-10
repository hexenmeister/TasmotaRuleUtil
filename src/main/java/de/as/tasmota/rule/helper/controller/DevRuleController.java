package de.as.tasmota.rule.helper.controller;

import de.as.tasmota.rule.helper.model.DevRuleModel;
import de.as.tasmota.rule.helper.services.DeviceConnectionParameters;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.CommandResult;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.DeviceAccessException;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.InvalideConnectionParametersException;
import de.as.utils.json.JsonData;

public class DevRuleController extends ControllerBase<DevRuleModel, RuleEditorController> {

    public DevRuleController(DevRuleModel model, RuleEditorController root) {
        super(model, root);
    }

    public void actionUploadToDevice() {
        // TODO Auto-generated method stub
        System.out.println("TODO: action performed: upload");
    }

    public void actionDownloadFromDevice() {
        String urlBase = this.getModel().getRoot().getOptionsHttpModel().getOptIp();
        String user = this.getModel().getRoot().getOptionsHttpModel().getOptUser();
        String pass = this.getModel().getRoot().getOptionsHttpModel().getOptPass();
        try {
            TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
                    new DeviceConnectionParameters(urlBase, user, pass));
            CommandResult response = tc.excuteCommand("Status 1");

            if (response.isSuccessful()) {
                JsonData jd = response.getData().getPath("Rules");
                String ruleText = jd != null ? jd.getValue().toString() : "<empty>";
                this.getModel().setRuleText(ruleText);
            } else {
                String warning = response.getErrorText();
                this.getModel().setRuleText("Error getting rules:\r\n" + warning);
            }
        } catch (InvalideConnectionParametersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DeviceAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
