package de.as.utils.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JsonDataTest {

    @Test
    public void testIsArray() {
        JsonData js;
        js = JsonParser.parseJson("[1,2,3]");
        assertTrue(js.isArray());
        js = JsonParser.parseJson("{\"k\":\"v\"}");
        assertFalse(js.isArray());
        assertFalse(js.getStrPath("k").isArray());

        js = JsonParser.parseJson("1");
        assertFalse(js.isArray());
        js = JsonParser.parseJson("\"1\"");
        assertFalse(js.isArray());
        js = JsonParser.parseJson("\"A\"");
        assertFalse(js.isArray());
    }

    @Test
    public void testIsObject() {
        JsonData js;
        js = JsonParser.parseJson("{}");
        assertTrue(js.isObject());
        js = JsonParser.parseJson("[1,2,3]");
        assertFalse(js.isObject());
        js = JsonParser.parseJson("{\"k\":\"v\"}");
        assertTrue(js.isObject());
        assertFalse(js.getStrPath("k").isObject());

        js = JsonParser.parseJson("1");
        assertFalse(js.isObject());
        js = JsonParser.parseJson("\"1\"");
        assertFalse(js.isObject());
        js = JsonParser.parseJson("\"A\"");
        assertFalse(js.isObject());
    }

    @Test
    public void testIsValue() {
        JsonData js;
        js = JsonParser.parseJson("{}");
        assertFalse(js.isValue());
        js = JsonParser.parseJson("\"A\"");
        assertTrue(js.isValue());
        js = JsonParser.parseJson("1");
        assertTrue(js.isValue());
        js = JsonParser.parseJson("[1,2,3]");
        assertFalse(js.isValue());
        js = JsonParser.parseJson("{\"k\":\"v\"}");
        assertFalse(js.isValue());
        assertTrue(js.getStrPath("k").isValue());

        js = JsonParser.parseJson("1");
        assertTrue(js.isValue());
        js = JsonParser.parseJson("\"1\"");
        assertTrue(js.isValue());
        js = JsonParser.parseJson("\"A\"");
        assertTrue(js.isValue());
    }

    @Test
    public void testGetEntries() {
        JsonData js;
        js = JsonParser.parseJson("{\"k\":\"v\"}");
        Map<String, JsonData> m = js.getEntries();
        assertTrue(m.keySet().size() == 1);
        assertEquals(m.get("k").getValue(), "v");
        assertNull(js.getElements());
    }

    @Test
    public void testGetElements() {
        JsonData js;
        js = JsonParser.parseJson("[1,\"A\",3]");
        List<JsonData> m = js.getElements();
        assertTrue(m.size() == 3);
        assertEquals(m.get(0).getValue().toString(), "1");
        assertEquals(m.get(1).getValue().toString(), "A");
        assertEquals(m.get(2).getValue().toString(), "3");
        assertNull(js.getEntries());
    }

    @Test
    public void testGetValue() {
        JsonData js;
        js = JsonParser.parseJson("{\"k\":\"v\"}");
        Map<String, JsonData> m = js.getEntries();
        assertNotNull(m.get("k").getValue());
        assertNull(m.get("X"));

        js = JsonParser.parseJson("[1,\"A\",3]");
        List<JsonData> l = js.getElements();
        assertNotNull(l.get(0).getValue());
    }

    @Test
    public void testGetPathStringArray() {
        JsonData js;
        js = JsonParser.parseJson(
                "{\"Status\":{\"Module\":0,\"FriendlyName\":[\"Tasmota\"],\"Topic\":\"fl01\",\"ButtonTopic\":\"\",\"Power\":1,\"PowerOnState\":0,\"LedState\":1,\"LedMask\":\"FFFF\",\"SaveData\":1,\"SaveState\":1,\"SwitchTopic\":\"\",\"SwitchMode\":[2,0,0,0,0,0,0,0],\"ButtonRetain\":0,\"SwitchRetain\":0,\"SensorRetain\":0,\"PowerRetain\":1}}");
        assertNull(js.getStrPath(""));
        assertNull(js.getStrPath("A"));
        assertNull(js.getStrPath("1"));
        assertEquals(js.getPath("Status", "Module").toString(), "0");
        assertEquals(js.getPath("Status", "LedMask").toString(), "\"FFFF\"");
        assertEquals(js.getPath("Status", "SwitchMode", "0").toString(), "2");
        assertEquals(js.getPath("Status", "SwitchMode", "1").toString(), "0");
        assertNull(js.getPath("Status", "SwitchMode", "99"));
        assertEquals(js.getPath("Status", "SwitchTopic").toString(), "\"\"");
    }

    @Test
    public void testGetPathString() {
        JsonData js;
        js = JsonParser.parseJson(
                "{\"Status\":{\"Module\":0,\"FriendlyName\":[\"Tasmota\"],\"Topic\":\"fl01\",\"ButtonTopic\":\"\",\"Power\":1,\"PowerOnState\":0,\"LedState\":1,\"LedMask\":\"FFFF\",\"SaveData\":1,\"SaveState\":1,\"SwitchTopic\":\"\",\"SwitchMode\":[2,0,0,0,0,0,0,0],\"ButtonRetain\":0,\"SwitchRetain\":0,\"SensorRetain\":0,\"PowerRetain\":1}}");
        assertNull(js.getStrPath(""));
        assertNull(js.getStrPath("A"));
        assertNull(js.getStrPath("1"));
        assertEquals(js.getStrPath("Status.Module").toString(), "0");
        assertEquals(js.getStrPath("Status.LedMask").toString(), "\"FFFF\"");
        assertEquals(js.getStrPath("Status.SwitchMode.0").toString(), "2");
        assertEquals(js.getStrPath("Status.SwitchMode.1").toString(), "0");
        assertNull(js.getStrPath("Status.SwitchMode.99"));
        assertEquals(js.getStrPath("Status.SwitchTopic").toString(), "\"\"");
    }

}
