
package de.as.tasmota.rule.helper.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import de.as.tasmota.rule.helper.model.RuleEditorModel;

public class TasmotaRuleHelperGui {

//    private RuleEditorController controller;

    private JFrame frame;
    private JSplitPane spEditor;
    private JPanel pDevice;
    private RuleEditorPanel ruleEditor;
    private DevRulePanel rulePanel1;
    private DevRulePanel rulePanel2;
    private DevRulePanel rulePanel3;
    private JPanel pTasmotaOptions;
    private JSplitPane spMain;
    private JPanel pEditor;
    private JPanel pTasm;
//    private JLabel lblTasmota;
//    private JLabel lblIp;
//    private JTextField tfUrl;
//    private JLabel lblNewLabel;
//    private JTextField tfUser;
//    private JLabel lblNewLabel_1;
//    private JTextField tfPass;
//    private JButton btnLoadAll;

//    /**
//     * Launch the application.
//     */
//    public static void main(String[] args) {
//	EventQueue.invokeLater(new Runnable() {
//	    public void run() {
//		try {
//		    RuleEditorModel model = new RuleEditorModel();
//		    TasmotaRuleHelperGui window = new TasmotaRuleHelperGui(model);
//		    window.frame.setSize(1000, 750);
////		    window.frame.pack();
//		    window.frame.setVisible(true);
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//	    }
//	});
//    }

    /**
     * Create the application.
     */
    public TasmotaRuleHelperGui(RuleEditorModel model) {
        this.initialize(model);
//	initModel(controller.getModel());
    }

    public void show() {
        this.frame.setSize(1200, 750);
        // this.frame.pack();
        this.frame.setVisible(true);
    }

//    private void initModel(RuleEditorModel model) {
//	// NOP
//    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(RuleEditorModel model) {
//	this.controller= controller;
        // Frame
        this.frame = new JFrame();
        this.frame.setTitle("Tasmota Rule Util");
        this.frame.setBounds(100, 100, 896, 727);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.spMain = new JSplitPane();
        this.frame.getContentPane().add(this.spMain, BorderLayout.CENTER);

        this.pEditor = new JPanel();
        this.spMain.setLeftComponent(this.pEditor);
        this.spMain.setDividerLocation(850);
        this.spMain.setResizeWeight(0.75);
        this.pEditor.setLayout(new BorderLayout(0, 0));

        // Main pane
        this.spEditor = new JSplitPane();
        this.pEditor.add(this.spEditor, BorderLayout.CENTER);

        this.pDevice = new JPanel();
        this.spEditor.setRightComponent(this.pDevice);

        this.ruleEditor = new RuleEditorPanel(
                model/*
                      * , new TextEvent() {
                      *
                      * @Override public void textReceived(String text) { RuleScript script =
                      * RuleParser.parse(text); String r1 = ""; String r2 = ""; String r3 = "";
                      * boolean found = false; for (Part part : script.getChildren()) { if (part
                      * instanceof RuleBlock) { found = true; if (((RuleBlock)
                      * part).getName().equalsIgnoreCase("rule1")) { r1 = ((RuleBlock)
                      * part).write(false); } if (((RuleBlock)
                      * part).getName().equalsIgnoreCase("rule2")) { r2 = ((RuleBlock)
                      * part).write(false); } if (((RuleBlock)
                      * part).getName().equalsIgnoreCase("rule3")) { r3 = ((RuleBlock)
                      * part).write(false); } } }
                      *
                      * if (!found) { r1 = script.write(); }
                      *
                      * RulePacker packer = new RulePacker(); List<String> rpacked1 =
                      * packer.pack(r1); List<String> rpacked2 = packer.pack(r2); List<String>
                      * rpacked3 = packer.pack(r3); // TODO rulePanel1.setText(rpacked1.get(0));
                      * rulePanel2.setText(rpacked2.get(0)); rulePanel3.setText(rpacked3.get(0)); } }
                      */);
        this.spEditor.setLeftComponent(this.ruleEditor);
        this.spEditor.setDividerLocation(500);
        this.spEditor.setResizeWeight(0.75);

        GridBagLayout gbl_pDevice = new GridBagLayout();
        gbl_pDevice.columnWidths = new int[] { 250, 0 };
        gbl_pDevice.rowHeights = new int[] { 32, 71, 71, 71 };
        gbl_pDevice.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
        gbl_pDevice.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
        this.pDevice.setLayout(gbl_pDevice);

        // Options pane
        this.pTasmotaOptions = new HttpConnectionOptionsPanel(model.getOptionsHttpModel());
        GridBagConstraints gbc_pTasmotaOptions = new GridBagConstraints();
        gbc_pTasmotaOptions.fill = GridBagConstraints.BOTH;
        gbc_pTasmotaOptions.weightx = 1.0;
        gbc_pTasmotaOptions.insets = new Insets(3, 0, 3, 0);
        gbc_pTasmotaOptions.gridx = 0;
        gbc_pTasmotaOptions.gridy = 0;
        this.pDevice.add(this.pTasmotaOptions, gbc_pTasmotaOptions);

        // Rule panels
        this.rulePanel1 = new DevRulePanel(model.getDevRuleModel1());
        GridBagConstraints gbc_spCc1 = new GridBagConstraints();
        gbc_spCc1.weighty = 1.0;
        gbc_spCc1.weightx = 1.0;
        gbc_spCc1.fill = GridBagConstraints.BOTH;
        gbc_spCc1.insets = new Insets(0, 0, 5, 0);
        gbc_spCc1.gridx = 0;
        gbc_spCc1.gridy = 1;
        this.pDevice.add(this.rulePanel1, gbc_spCc1);

        this.rulePanel2 = new DevRulePanel(model.getDevRuleModel2());
        GridBagConstraints gbc_spCc2 = new GridBagConstraints();
        gbc_spCc2.weighty = 1.0;
        gbc_spCc2.weightx = 1.0;
        gbc_spCc2.fill = GridBagConstraints.BOTH;
        gbc_spCc2.insets = new Insets(0, 0, 5, 0);
        gbc_spCc2.gridx = 0;
        gbc_spCc2.gridy = 2;
        this.pDevice.add(this.rulePanel2, gbc_spCc2);

        this.rulePanel3 = new DevRulePanel(model.getDevRuleModel3());
        GridBagConstraints gbc_spCc3 = new GridBagConstraints();
        gbc_spCc3.weighty = 1.0;
        gbc_spCc3.weightx = 1.0;
        gbc_spCc3.fill = GridBagConstraints.BOTH;
        gbc_spCc3.gridx = 0;
        gbc_spCc3.gridy = 3;
        this.pDevice.add(this.rulePanel3, gbc_spCc3);

        this.pTasm = new JPanel();
        this.spMain.setRightComponent(this.pTasm);
    }

}
