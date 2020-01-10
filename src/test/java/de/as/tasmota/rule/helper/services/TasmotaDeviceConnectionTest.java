package de.as.tasmota.rule.helper.services;

import org.junit.Test;

import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.CommandResult;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.DeviceAccessException;
import de.as.tasmota.rule.helper.services.TasmotaDeviceConnection.InvalideConnectionParametersException;
import de.as.utils.json.JsonData;
import junit.framework.Assert;

public class TasmotaDeviceConnectionTest {

    private static final String PASS = "ac7kdy";

    @Test
    public void testExecuteCommandStr() throws InvalideConnectionParametersException, DeviceAccessException {
        TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
                new DeviceConnectionParameters("192.168.0.70", "admin", PASS));
        String response;

        response = tc.excuteCommandStr("power1");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("dummy");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 0");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 1");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 2");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 3");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 4");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 5");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 6");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 7");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 8");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 9");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 10");
        System.out.println(response);
        Assert.assertNotNull(response);

        response = tc.excuteCommandStr("Status 11");
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

    @Test
    public void testExecuteCommand() throws InvalideConnectionParametersException, DeviceAccessException {
        TasmotaDeviceConnection tc = new TasmotaDeviceConnection(
                new DeviceConnectionParameters("192.168.0.70", "admin", PASS));
        CommandResult response;

        response = tc.excuteCommand("Status 1");
        Assert.assertTrue(response.isSuccessful());
        Assert.assertFalse(response.isUnknown());
        Assert.assertNull(response.getErrorText());
        Assert.assertNotNull(response.getData());

        response = tc.excuteCommand("dummy");
        Assert.assertFalse(response.isSuccessful());
        Assert.assertTrue(response.isUnknown());
        Assert.assertNotNull(response.getErrorText());
        Assert.assertNotNull(response.getData());

        // Kein passwort
        tc = new TasmotaDeviceConnection(new DeviceConnectionParameters("192.168.0.70", "admin", ""));
        response = tc.excuteCommand("Status 1");
        Assert.assertFalse(response.isSuccessful());
        Assert.assertFalse(response.isUnknown());
        Assert.assertNotNull(response.getErrorText());
        Assert.assertNotNull(response.getData());

        response = tc.excuteCommand("dummy");
        Assert.assertFalse(response.isSuccessful());
        Assert.assertFalse(response.isUnknown());
        Assert.assertNotNull(response.getErrorText());
        Assert.assertNotNull(response.getData());

        // Falsche IP
        tc = new TasmotaDeviceConnection(new DeviceConnectionParameters("192.168.0.1", "admin", ""));
        response = tc.excuteCommand("Status 1");
        Assert.assertFalse(response.isSuccessful());
        Assert.assertFalse(response.isUnknown());
        Assert.assertNotNull(response.getErrorText());
        Assert.assertNull(response.getData());
        Assert.assertNotNull(response.getException());

        response = tc.excuteCommand("dummy");
        Assert.assertFalse(response.isSuccessful());
        Assert.assertFalse(response.isUnknown());
        Assert.assertNotNull(response.getErrorText());
        Assert.assertNull(response.getData());
        Assert.assertNotNull(response.getException());
    }
}
