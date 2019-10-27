package de.as.tasmota.rule.helper.gui.utils;

import java.util.EventListener;

public interface TextEvent extends EventListener {
    public void textReceived(String text);
}
