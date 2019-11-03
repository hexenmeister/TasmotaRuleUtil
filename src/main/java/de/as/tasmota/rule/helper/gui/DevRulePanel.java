package de.as.tasmota.rule.helper.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.as.tasmota.rule.helper.controller.DevRuleController;
import de.as.tasmota.rule.helper.model.DevRuleModel;
import de.as.tasmota.rule.helper.model.ModelBase.ValueBridge;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DevRulePanel extends JPanel {

    private static final long serialVersionUID = -3879865029886567323L;
    
    private JTextArea taRule;
    private JLabel lSettings;

    public DevRulePanel(DevRuleController controller) {
	// this.transformEvent = transformEvent;
	setLayout(new BorderLayout(0, 0));
	initialize(controller);
	initModel(controller.getModel());
    }

    private void initModel(DevRuleModel model) {
	model.addBridge(DevRuleModel.KEY_DEV_RULE_TEXT, new ValueBridge<String>() {

	    @Override
	    public String getValue() {
		return taRule.getText();
	    }

	    @Override
	    public void setValue(String value) {
		taRule.setText(value);
	    }
	});

	model.addBridge(DevRuleModel.KEY_DEV_INFO_TEXT, new ValueBridge<String>() {

	    @Override
	    public String getValue() {
		return lSettings.getText();
	    }

	    @Override
	    public void setValue(String value) {
		lSettings.setText(value);
	    }
	});
    }

    public void setText(String text) {
	taRule.setText(text);
    }

    public String getText() {
	return taRule.getText();
    }

    private void initialize(DevRuleController controller) {
	JScrollPane sp1 = new JScrollPane();
	this.add(sp1, BorderLayout.CENTER);

//	GridBagConstraints gbc_spCc1 = new GridBagConstraints();
//	gbc_spCc1.weighty = 1.0;
//	gbc_spCc1.weightx = 1.0;
//	gbc_spCc1.fill = GridBagConstraints.BOTH;
//	gbc_spCc1.insets = new Insets(0, 0, 5, 0);
//	gbc_spCc1.gridx = 0;
//	gbc_spCc1.gridy = 1;
//	this.pCc.add(this.spCc1, gbc_spCc1);

	this.taRule = new JTextArea();
	this.taRule.setBorder(BorderFactory.createEtchedBorder());
	this.taRule.setWrapStyleWord(true);
	this.taRule.setLineWrap(true);
	sp1.setViewportView(this.taRule);

	JPanel pO = new JPanel();
	add(pO, BorderLayout.NORTH);
	pO.setBorder(BorderFactory.createEtchedBorder());
	GridBagLayout gbl_pO = new GridBagLayout();
	gbl_pO.columnWidths = new int[] { 0, 0, 0, 0, 0 };
	gbl_pO.rowHeights = new int[] { 22, 22, 0 };
	gbl_pO.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
	gbl_pO.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
	pO.setLayout(gbl_pO);

	JLabel lName = new JLabel(controller.getModel().getLabel());
	lName.setFont(new Font("Tahoma", Font.BOLD, 11));
	pO.add(lName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		GridBagConstraints.HORIZONTAL, new Insets(3, 7, 3, 7), 0, 0));

	lSettings = new JLabel("---");
	lSettings.setFont(new Font("Tahoma", Font.PLAIN, 11));
	GridBagConstraints gbc_lSettings = new GridBagConstraints();
	gbc_lSettings.fill = GridBagConstraints.HORIZONTAL;
	gbc_lSettings.anchor = GridBagConstraints.WEST;
	gbc_lSettings.weightx = 1.0;
	gbc_lSettings.insets = new Insets(3, 7, 3, 7);
	gbc_lSettings.gridx = 1;
	gbc_lSettings.gridy = 0;
	pO.add(lSettings, gbc_lSettings);
	
	JLabel label = new JLabel("---");
	label.setFont(new Font("Tahoma", Font.PLAIN, 11));
	GridBagConstraints gbc_label = new GridBagConstraints();
	gbc_label.fill = GridBagConstraints.HORIZONTAL;
	gbc_label.anchor = GridBagConstraints.WEST;
	gbc_label.insets = new Insets(3, 7, 3, 7);
	gbc_label.gridx = 1;
	gbc_label.gridy = 1;
	pO.add(label, gbc_label);

	JPanel pLf = new JPanel();
	add(pLf, BorderLayout.WEST);
	GridBagLayout gbl_pLf = new GridBagLayout();
	gbl_pLf.columnWidths = new int[] { 0, 0 };
	gbl_pLf.rowHeights = new int[] { 0, 0, 0, 0, 0 };
	gbl_pLf.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
	gbl_pLf.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
	pLf.setLayout(gbl_pLf);

	JButton btnPack = new JButton("pack >");
	btnPack.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		controller.actionGetFromEditor();
	    }
	});
	GridBagConstraints gbc_btnPack = new GridBagConstraints();
	gbc_btnPack.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnPack.insets = new Insets(3, 3, 3, 3);
	gbc_btnPack.anchor = GridBagConstraints.NORTH;
	gbc_btnPack.gridx = 0;
	gbc_btnPack.gridy = 0;
	pLf.add(btnPack, gbc_btnPack);

	JButton button_1 = new JButton("< edit");
	button_1.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		controller.actionSendToEditor();
	    }
	});
	GridBagConstraints gbc_button_1 = new GridBagConstraints();
	gbc_button_1.fill = GridBagConstraints.HORIZONTAL;
	gbc_button_1.insets = new Insets(3, 3, 3, 3);
	gbc_button_1.gridx = 0;
	gbc_button_1.gridy = 1;
	pLf.add(button_1, gbc_button_1);

	JButton btnLd = new JButton("d.load");
	btnLd.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		controller.actionDownloadFromDevice();
	    }
	});
	GridBagConstraints gbc_btnLd = new GridBagConstraints();
	gbc_btnLd.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnLd.insets = new Insets(3, 3, 3, 3);
	gbc_btnLd.gridx = 0;
	gbc_btnLd.gridy = 2;
	pLf.add(btnLd, gbc_btnLd);

	JButton btnSd = new JButton("upload");
	btnSd.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		controller.actionUploadToDevice();
	    }
	});
	GridBagConstraints gbc_btnSd = new GridBagConstraints();
	gbc_btnSd.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnSd.insets = new Insets(3, 3, 3, 3);
	gbc_btnSd.gridx = 0;
	gbc_btnSd.gridy = 3;
	pLf.add(btnSd, gbc_btnSd);

    }
}
