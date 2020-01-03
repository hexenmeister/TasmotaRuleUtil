package de.as.tasmota.rule.helper.controller;

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

import de.as.tasmota.rule.helper.model.DevRuleModel;
import de.as.utils.http.ParameterStringBuilder;

public class DevRuleController extends ControllerBase<DevRuleModel, RuleEditorController> {

    public DevRuleController(DevRuleModel model, RuleEditorController root) {
	super(model, root);
    }

    public void actionUploadToDevice() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: upload");
    }

    public void actionDownloadFromDevice() {
	// TODO Auto-generated method stub
	System.out.println("TODO: action performed: download");

	// XXX: Test
	// http://192.168.0.70/cm?cmnd=Rule1&user=admin&password=xxx
	try {
	    String urlBase = this.getModel().getRoot().getOptionsHttpModel().getOptIp();
	    if (urlBase.trim().isEmpty()) {
		return;
	    }
	    if (!urlBase.toLowerCase().startsWith("http")) {
		urlBase = "http://" + urlBase;
	    }
	    if (!urlBase.endsWith("/")) {
		urlBase += "/";
	    }
	    String user = this.getModel().getRoot().getOptionsHttpModel().getOptUser();
	    if (user.trim().isEmpty()) {
		user = "admin";
	    }
	    String pass = this.getModel().getRoot().getOptionsHttpModel().getOptPass();
	    String urlFull = urlBase + "cm?cmnd=Rule" + this.getModel().getIndex();
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
		    getModel().setRuleText("<empty>");
		} else {
		    getModel().setRuleText(ruleText);
		}
	    } else {
		String warning = (String) jso.get("WARNING");
		System.out.println(warning);
		getModel().setRuleText("Error getting rules:\r\n" + warning);
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
