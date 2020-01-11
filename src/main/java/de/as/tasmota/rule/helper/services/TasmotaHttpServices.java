package de.as.tasmota.rule.helper.services;

import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.CommandResult;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.DeviceAccessException;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.InvalideConnectionParametersException;
import de.as.utils.json.JsonData;

public class TasmotaHttpServices {

    private static class Holder {
        static final TasmotaHttpServices INSTANCE = new TasmotaHttpServices();
    }

    public static TasmotaHttpServices getInstance() {
        return Holder.INSTANCE;
    }

    private TasmotaHttpServices() {
        // NOP
    }

    public String getTasmotaRuleText(String url, String user, String pass, int ruleIndex) {
        try {
            TasmotaDeviceConnection tc = new TasmotaDeviceConnection(new DeviceConnectionParameters(url, user, pass));
            CommandResult response = tc.excuteCommand("Rule" + ruleIndex);

            if (response.isSuccessful()) {
                JsonData jd = response.getData().getPath("Rules");
                if (jd == null) {
                    return "Problem getting rule text. The answer was " + response.getData();
                } else {
                    String ruleText = jd != null ? jd.getValue().toString() : "<empty>";
                    return ruleText;
                }
            } else {
                String warning = response.getErrorText();
                return "Error getting rules:\r\n" + warning;
            }
        } catch (InvalideConnectionParametersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DeviceAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "internal error";
    }
}
