package de.as.tasmota.rule.helper.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.as.utils.json.JsonData;
import de.as.utils.json.JsonParser;

public class TasmotaDeviceConnection {

    public static class CommandResult {
        private Exception exception;
        private boolean successful;
        private boolean unknown;
        private String errorText;
//        private String resultText;
        private JsonData data;
    }

    public static class InvalideConnectionParametersException extends Exception {

        private static final long serialVersionUID = 6627830417366011710L;

    }

    public static class DeviceAccessException extends Exception {

        private static final long serialVersionUID = 6627830417366011710L;

        private int status;

        public int getStatus() {
            return this.status;
        }

        public String getResp() {
            return this.resp;
        }

        private String resp;

        public DeviceAccessException(String msg, int status, String resp) {
            super(msg);
            this.status = status;
            this.resp = resp;
        }

        public DeviceAccessException(String msg, Exception cause) {
            super(msg, cause);
        }

    }

    public static class InternalException extends RuntimeException {

        private static final long serialVersionUID = 9030359275715814418L;

        public InternalException(String msg) {
            super(msg);
        }
    }

    private DeviceConnectionParameters params;

    public TasmotaDeviceConnection(DeviceConnectionParameters params) throws InvalideConnectionParametersException {
        this.params = params;
        if (!params.isValide()) {
            throw new InvalideConnectionParametersException();
        }
    }

    private HttpURLConnection createConnection(String cmd) throws IOException {
        String urlFull = this.params.getUrl() + "cm?cmnd=" + cmd;
        String user = this.params.getUser();
        String pass = this.params.getPass();
        if (!user.trim().isEmpty() && !pass.trim().isEmpty()) {
            urlFull += "&user=" + user + "&password=" + pass;
        }
        URL url = new URL(urlFull);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    private void closeConnection(HttpURLConnection con) {
        con.disconnect();
    }

    private String readResponse(InputStream stream) throws IOException {
        if (stream == null) {
            return null;
        }

        StringBuffer content;
        BufferedReader in = null;
        try {
            content = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(stream));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return content.toString();
    }

    public String sendReceive(String cmd) throws DeviceAccessException {
        HttpURLConnection con = null;
        try {
            con = this.createConnection(cmd);
            int status = con.getResponseCode();
            if (status > 299) {
                InputStream stream = con.getErrorStream();
                String response = this.readResponse(stream);
                throw new DeviceAccessException("error while http access. http response code: " + status, status,
                        response);
            } else {
                InputStream stream = con.getInputStream();
                String response = this.readResponse(stream);
                return response;
            }
        } catch (IOException e) {
            throw new DeviceAccessException("error while http access. cause: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                this.closeConnection(con);
            }
        }
    }

    public String excuteCommandStr(String cmd) throws DeviceAccessException {
        return this.sendReceive(cmd);
    }

    public JsonData excuteCommandJson(String cmd) throws DeviceAccessException {
        return JsonParser.parseJson(this.excuteCommandStr(cmd));
    }

    public CommandResult excuteCommand(String cmd) throws DeviceAccessException {
        CommandResult cr = new CommandResult();
        JsonData js;
        try {
            js = this.excuteCommandJson(cmd);
            cr.data = js;
            // TODO: Check unknown command, warning,..
            cr.successful = true;
        } catch (DeviceAccessException e) {
            cr.errorText = e.getMessage();
            cr.exception = e;
        }

        return cr;
    }
}
