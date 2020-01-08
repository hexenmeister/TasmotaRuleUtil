package de.as.utils.json;

import org.junit.Test;

import junit.framework.Assert;

public class JsonParserTest {

    @Test
    public void testParseJson() {
        test("{\"k1\":\"v1\"}");
        test("{\"Command\":\"Unknown\"}");
        test("{\"Status\":{\"Module\":0,\"FriendlyName\":[\"Tasmota_H801_FL01\"],\"Topic\":\"fl01\",\"ButtonTopic\":\"\",\"Power\":1,\"PowerOnState\":0,\"LedState\":1,\"LedMask\":\"FFFF\",\"SaveData\":1,\"SaveState\":1,\"SwitchTopic\":\"\",\"SwitchMode\":[2,0,0,0,0,0,0,0],\"ButtonRetain\":0,\"SwitchRetain\":0,\"SensorRetain\":0,\"PowerRetain\":1}}");
        test("{\"Rule1\":\"OFF\",\"Once\":\"OFF\",\"StopOnError\":\"OFF\",\"Free\":55,\"Rules\":\"on System#Boot do backlog power off; VAR1 0; VAR2 0; VAR3 99 endon\"}");
    }

    private static void test(String v) {
        String ret = JsonParser.parseJson(v).toString();
        Assert.assertEquals(ret, v);
    }

}
