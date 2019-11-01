package de.as.tasmota.rule.helper;

import java.awt.EventQueue;

import de.as.tasmota.rule.helper.controller.RuleEditorController;
import de.as.tasmota.rule.helper.gui.TasmotaRuleHelperGui;

public class Starter {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
//		    RuleEditorModel model = new RuleEditorModel();
		    RuleEditorController controller = new RuleEditorController();
		    TasmotaRuleHelperGui window = new TasmotaRuleHelperGui(controller);
		    window.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }
}
