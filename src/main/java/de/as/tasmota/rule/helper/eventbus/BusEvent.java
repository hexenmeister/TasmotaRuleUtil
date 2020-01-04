package de.as.tasmota.rule.helper.eventbus;

public class BusEvent {

    private String input;
    private Object[] params;

    public BusEvent(String input) {
	this.input = input;
    }

    public BusEvent(String input, Object... params) {
	this.input = input;
	this.params = params;
    }

    public String getValue() {
	return this.input;
    }

    public Object[] getParam() {
	return this.params;
    }

}
