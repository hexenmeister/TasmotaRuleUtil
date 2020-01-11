package de.as.tasmota.rule.helper.services;

public class DeviceConnectionParameters {

    private String url;
    private String user;
    private String pass;
    private boolean valide = false;
    private int timeoutSeconds = 1000;

    public DeviceConnectionParameters(String url, String user, String pass) {
        this(url, user, pass, 1000);
    }

    public DeviceConnectionParameters(String url, String user, String pass, int timeoutSeconds) {
        super();
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.timeoutSeconds = timeoutSeconds;
        this.init();
    }

    private void init() {
        String urlBase = this.getUrl();
        if (urlBase.trim().isEmpty()) {
            return;
        }
        if (!urlBase.toLowerCase().startsWith("http")) {
            urlBase = "http://" + urlBase;
        }
        if (!urlBase.endsWith("/")) {
            urlBase += "/";
        }
        String user = this.getUser();
        if (user.trim().isEmpty()) {
            user = "admin";
        }
        this.valide = true;
        this.url = urlBase;
        this.user = user;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUser() {
        return this.user;
    }

    public String getPass() {
        return this.pass;
    }

    public boolean isValide() {
        return this.valide;
    }

    public int getTimeout() {
        return this.timeoutSeconds;
    }
}
