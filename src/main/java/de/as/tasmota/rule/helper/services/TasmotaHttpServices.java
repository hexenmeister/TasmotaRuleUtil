package de.as.tasmota.rule.helper.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.as.utils.http.ParameterStringBuilder;

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

    public String getTasmotaRule(DeviceConnectionParameters params, int ruleIndex) {
        String ret = null;
        // http://192.168.0.70/cm?cmnd=Rule1&user=admin&password=xxx
        try {
            String urlBase = params.getUrl();
            if (urlBase.trim().isEmpty()) {
                return ret;
            }
            if (!urlBase.toLowerCase().startsWith("http")) {
                urlBase = "http://" + urlBase;
            }
            if (!urlBase.endsWith("/")) {
                urlBase += "/";
            }
            String user = params.getUser();
            if (user.trim().isEmpty()) {
                user = "admin";
            }
            String pass = params.getPass();
            String urlFull = urlBase + "cm?cmnd=Rule" + ruleIndex;
            if (!user.trim().isEmpty() && !pass.trim().isEmpty()) {
                urlFull += "&user=" + user + "&password=" + pass;
            }
            URL url = new URL(urlFull);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("param1", "val");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            int status = con.getResponseCode();

            InputStream streamReader;
            if (status > 299) {
                streamReader = con.getErrorStream();
            } else {
                streamReader = con.getInputStream();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(streamReader));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            if (status > 299) {
                System.err.println(content);
            } else {
                System.out.println(content);
            }

            JSONParser parser = new JSONParser();
            JSONObject jso = (JSONObject) parser.parse(content.toString());
            String ruleText = (String) jso.get("Rules");
            if (ruleText != null) {
                System.out.println(ruleText);
                if (ruleText.trim().isEmpty()) {
                    ret = "<empty>";
                } else {
                    ret = ruleText;
                }
            } else {
                String warning = (String) jso.get("WARNING");
                System.out.println(warning);
                ret = "Error getting rules:\r\n" + warning;
            }

            con.disconnect();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }
}
