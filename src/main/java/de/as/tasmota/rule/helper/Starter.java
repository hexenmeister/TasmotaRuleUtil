package de.as.tasmota.rule.helper;

import java.awt.EventQueue;

import de.as.tasmota.rule.helper.gui.TasmotaRuleHelperGui;
import de.as.tasmota.rule.helper.model.RuleEditorModel;

public class Starter {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
//		    RuleEditorModel model = new RuleEditorModel();
//		    RuleEditorController controller = new RuleEditorController();
//		    TasmotaRuleHelperGui window = new TasmotaRuleHelperGui(controller);
                    RuleEditorModel model = new RuleEditorModel();
                    TasmotaRuleHelperGui window = new TasmotaRuleHelperGui(model);
                    window.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
