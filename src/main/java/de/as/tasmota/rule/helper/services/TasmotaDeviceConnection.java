package de.as.tasmota.rule.helper.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import de.as.utils.json.JsonData;
import de.as.utils.json.JsonParser;

public class TasmotaDeviceConnection {

    public static class CommandResult {
        public Exception getException() {
            return this.exception;
        }

        public boolean isSuccessful() {
            return this.successful;
        }

        public boolean isUnknown() {
            return this.unknown;
        }

        public String getErrorText() {
            return this.errorText;
        }

        public JsonData getData() {
            return this.data;
        }

        Exception exception;
        boolean successful;
        boolean unknown;
        String errorText;
//        String resultText;
        JsonData data;
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
        String urlFull = this.params.getUrl() + "cm?cmnd=" + this.encodeValue(cmd);
        String user = this.params.getUser();
        String pass = this.params.getPass();
        if (!user.trim().isEmpty() && !pass.trim().isEmpty()) {
            urlFull += "&user=" + user + "&password=" + pass;
        }
        URL url = new URL(urlFull);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        // Test Connection with HEAD Method and low timeout?
        con.setConnectTimeout(3000); //set timeout to 5 seconds
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

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
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
            cr.successful = true;
            cr.unknown = false;
            cr.data = js;

            JsonData jd;
            // {"WARNING":"Need user=<username>&password=<password>"}
            jd = js.getPath("WARNING");
            if (jd != null) {
                cr.successful = false;
                cr.errorText = jd.getValue().toString();
            }

            // {"Command":"Unknown"}
            jd = js.getPath("Command");
            if (jd != null && "Unknown".equalsIgnoreCase(jd.getValue().toString())) {
                cr.successful = false;
                cr.unknown = true;
                cr.errorText = "Command unknown";
            }

            // Weiteres?

        } catch (DeviceAccessException e) {
            cr.errorText = e.getMessage();
            cr.exception = e;
        }

        return cr;
    }
}
