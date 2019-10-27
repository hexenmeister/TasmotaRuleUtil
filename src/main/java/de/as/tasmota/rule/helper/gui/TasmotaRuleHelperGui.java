
package de.as.tasmota.rule.helper.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import de.as.tasmota.rule.helper.RulePacker;
import de.as.tasmota.rule.helper.gui.utils.TextEvent;
import de.as.tasmota.rule.helper.model.RuleEditorModel;

public class TasmotaRuleHelperGui {

    private RuleEditorModel model;
    private JFrame frame;
    private JSplitPane spMain;
    private JPanel pDevice;
    private RuleEditorPanel ruleEditor;
    private DevRulePanel rulePanel1;
    private DevRulePanel rulePanel2;
    private DevRulePanel rulePanel3;
    private JPanel pTasmotaOptions;
//    private JLabel lblTasmota;
//    private JLabel lblIp;
//    private JTextField tfUrl;
//    private JLabel lblNewLabel;
//    private JTextField tfUser;
//    private JLabel lblNewLabel_1;
//    private JTextField tfPass;
//    private JButton btnLoadAll;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    RuleEditorModel model = new RuleEditorModel();
		    TasmotaRuleHelperGui window = new TasmotaRuleHelperGui(model);
		    window.frame.setSize(1000, 750);
//		    window.frame.pack();
		    window.frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the application.
     */
    public TasmotaRuleHelperGui(RuleEditorModel model) {
	initialize(model);
	initModel(model);
    }

    private void initModel(RuleEditorModel model) {
	// NOP
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(RuleEditorModel model) {
	this.model = model;
	// Frame
	this.frame = new JFrame();
	this.frame.setTitle("Tasmota Rule Util");
	this.frame.setBounds(100, 100, 896, 727);
	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Main pane
	this.spMain = new JSplitPane();
	this.frame.getContentPane().add(this.spMain, BorderLayout.CENTER);

	this.pDevice = new JPanel();
	this.spMain.setRightComponent(this.pDevice);

	this.ruleEditor = new RuleEditorPanel(model, new TextEvent() {
	    @Override
	    public void textReceived(String text) {
		RulePacker packer = new RulePacker();
		List<String> rpacked = packer.pack(text);
		// TODO
		rulePanel1.setText(rpacked.get(0));
	    }
	});
	this.spMain.setLeftComponent(this.ruleEditor);
	this.spMain.setDividerLocation(600);

	GridBagLayout gbl_pDevice = new GridBagLayout();
	gbl_pDevice.columnWidths = new int[] { 250, 0 };
	gbl_pDevice.rowHeights = new int[] { 32, 71, 71, 71 };
	gbl_pDevice.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
	gbl_pDevice.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
	pDevice.setLayout(gbl_pDevice);

	// Options pane
	pTasmotaOptions = new HttpConnectionOptionsPanel(model.getOptionsHttpModel());
	GridBagConstraints gbc_pTasmotaOptions = new GridBagConstraints();
	gbc_pTasmotaOptions.fill = GridBagConstraints.BOTH;
	gbc_pTasmotaOptions.weightx = 1.0;
	gbc_pTasmotaOptions.insets = new Insets(3, 0, 3, 0);
	gbc_pTasmotaOptions.gridx = 0;
	gbc_pTasmotaOptions.gridy = 0;
	pDevice.add(pTasmotaOptions, gbc_pTasmotaOptions);

	// Rule panels
	rulePanel1 = new DevRulePanel(model.getDevRuleModel1());
	GridBagConstraints gbc_spCc1 = new GridBagConstraints();
	gbc_spCc1.weighty = 1.0;
	gbc_spCc1.weightx = 1.0;
	gbc_spCc1.fill = GridBagConstraints.BOTH;
	gbc_spCc1.insets = new Insets(0, 0, 5, 0);
	gbc_spCc1.gridx = 0;
	gbc_spCc1.gridy = 1;
	this.pDevice.add(rulePanel1, gbc_spCc1);

	rulePanel2 = new DevRulePanel(model.getDevRuleModel2());
	GridBagConstraints gbc_spCc2 = new GridBagConstraints();
	gbc_spCc2.weighty = 1.0;
	gbc_spCc2.weightx = 1.0;
	gbc_spCc2.fill = GridBagConstraints.BOTH;
	gbc_spCc2.insets = new Insets(0, 0, 5, 0);
	gbc_spCc2.gridx = 0;
	gbc_spCc2.gridy = 2;
	this.pDevice.add(this.rulePanel2, gbc_spCc2);

	rulePanel3 = new DevRulePanel(model.getDevRuleModel3());
	GridBagConstraints gbc_spCc3 = new GridBagConstraints();
	gbc_spCc3.weighty = 1.0;
	gbc_spCc3.weightx = 1.0;
	gbc_spCc3.fill = GridBagConstraints.BOTH;
	gbc_spCc3.gridx = 0;
	gbc_spCc3.gridy = 3;
	this.pDevice.add(this.rulePanel3, gbc_spCc3);
    }

}
