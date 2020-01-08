package de.as.tasmota.rule.helper.services;

import org.junit.Test;

import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.DeviceAccessException;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.InvalideConnectionParametersException;
import de.as.utils.json.JsonData;
import junit.framework.Assert;

public class TasmotaDeviceConnectionTest {

    private static final String PASS = "geheim";

    @Test
    public void testCommand() throws InvalideConnectionParametersException, DeviceAccessException {
        TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
                new DeviceConnectionParameters("192.168.0.70", "admin", PASS));
//        String response = tc.excuteCommandStr("power1");
        String response = tc.excuteCommandStr("dummy");
//        String response = tc.excuteCommandStr("Status1");
        System.out.println(response);
        Assert.assertNotNull(response);
    }

    @Test
    public void testCommandJson() throws InvalideConnectionParametersException, DeviceAccessException {
        TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
                new DeviceConnectionParameters("192.168.0.70", "admin", PASS));
//        MJsonData response = tc.excuteCommandJson("power1");
//        MJsonData response = tc.excuteCommandJson("dummy");
        JsonData response = tc.excuteCommandJson("Status1");
        System.out.println(response);
        Assert.assertNotNull(response);
    }

    @Test
    public void testCommandJsonNoPass() throws InvalideConnectionParametersException, DeviceAccessException {
        TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
                new DeviceConnectionParameters("192.168.0.70", "admin", ""));
        JsonData response = tc.excuteCommandJson("Status1");
        System.out.println(response);
        Assert.assertNotNull(response);
    }

    @Test
    public void testCommandJsonGetPath() throws InvalideConnectionParametersException, DeviceAccessException {
        TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
                new DeviceConnectionParameters("192.168.0.70", "admin", PASS));
        JsonData response = tc.excuteCommandJson("Status1");
        JsonData node = response.getStrPath("Status.FriendlyName.0");
        System.out.println(node);
        Assert.assertNotNull(node);
        node = response.getStrPath("Status.PowerOnState");
        System.out.println(node);
        Assert.assertNotNull(node);
        node = response.getStrPath("Status.SwitchMode");
        System.out.println(node);
        Assert.assertNotNull(node);
        node = response.getStrPath("Status.SwitchMode.0");
        System.out.println(node);
        Assert.assertNotNull(node);
        node = response.getStrPath("X.Y.Z");
        System.out.println(node);
        Assert.assertNull(node);
    }
}
