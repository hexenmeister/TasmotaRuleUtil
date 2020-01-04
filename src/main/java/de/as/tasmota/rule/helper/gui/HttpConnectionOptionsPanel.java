package de.as.tasmota.rule.helper.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.as.tasmota.rule.helper.controller.OptionsHttpController;
import de.as.tasmota.rule.helper.eventbus.BusEvent;
import de.as.tasmota.rule.helper.eventbus.EventRegistry;
import de.as.tasmota.rule.helper.eventbus.Registration;
import de.as.tasmota.rule.helper.model.DevRuleModel;
import de.as.tasmota.rule.helper.model.OptionsHttpModel;

public class HttpConnectionOptionsPanel extends JPanel {

    private static final long serialVersionUID = -5740429526128477409L;

    private static final EventRegistry<String, BusEvent> EVENT_BUS = EventRegistry.instance();

    public Registration register(String key, Consumer<BusEvent> listener) {
	return EVENT_BUS.register(key, listener);
    }

    private List<Registration> registrations = new ArrayList<>();

    public void release() {
	this.registrations.forEach(Registration::remove);
    }

    private JTextField tfUrl;
    private JTextField tfPass;
    private JTextField tfUser;

    /**
     * Create the panel.
     */
    public HttpConnectionOptionsPanel(OptionsHttpController controller) {
	initialize(controller);
	initModel(controller.getModel());
    }

    private void initModel(OptionsHttpModel model) {
	this.registrations.add(model.registerStringBridge(OptionsHttpModel.KEY_OPT_IP, () -> tfUrl.getText(),
		(v) -> tfUrl.setText(v)));
	this.registrations.add(model.registerStringBridge(OptionsHttpModel.KEY_OPT_PASS, () -> tfPass.getText(),
		(v) -> tfPass.setText(v)));
	this.registrations.add(model.registerStringBridge(OptionsHttpModel.KEY_OPT_USER, () -> tfUser.getText(),
		(v) -> tfUser.setText(v)));
    }

    private void initialize(OptionsHttpController controller) {
	GridBagLayout gbl_pTasmotaOptions = new GridBagLayout();
	gbl_pTasmotaOptions.rowHeights = new int[] { 22, 22, 0, 0 };
	gbl_pTasmotaOptions.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
	gbl_pTasmotaOptions.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
	this.setLayout(gbl_pTasmotaOptions);

	JLabel lblTasmota = new JLabel("Connection");
	lblTasmota.setFont(new Font("Tahoma", Font.BOLD, 11));
	GridBagConstraints gbc_lblTasmota = new GridBagConstraints();
	gbc_lblTasmota.gridwidth = 2;
	gbc_lblTasmota.weighty = 1.0;
	gbc_lblTasmota.fill = GridBagConstraints.HORIZONTAL;
	gbc_lblTasmota.anchor = GridBagConstraints.WEST;
	gbc_lblTasmota.insets = new Insets(3, 7, 5, 15);
	gbc_lblTasmota.gridx = 0;
	gbc_lblTasmota.gridy = 0;
	this.add(lblTasmota, gbc_lblTasmota);

	JLabel lblIp = new JLabel("IP:");
	lblIp.setFont(new Font("Tahoma", Font.PLAIN, 11));
	GridBagConstraints gbc_lblIp = new GridBagConstraints();
	gbc_lblIp.weighty = 1.0;
	gbc_lblIp.anchor = GridBagConstraints.WEST;
	gbc_lblIp.insets = new Insets(3, 10, 5, 5);
	gbc_lblIp.gridx = 1;
	gbc_lblIp.gridy = 1;
	this.add(lblIp, gbc_lblIp);

	tfUrl = new JTextField();
	tfUrl.setColumns(10);
	GridBagConstraints gbc_tfUrl = new GridBagConstraints();
	gbc_tfUrl.gridwidth = 3;
	gbc_tfUrl.fill = GridBagConstraints.HORIZONTAL;
	gbc_tfUrl.weightx = 1.0;
	gbc_tfUrl.anchor = GridBagConstraints.WEST;
	gbc_tfUrl.insets = new Insets(3, 0, 5, 5);
	gbc_tfUrl.gridx = 2;
	gbc_tfUrl.gridy = 1;
	this.add(tfUrl, gbc_tfUrl);

	JLabel lblNewLabel = new JLabel("User:");
	lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
	GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
	gbc_lblNewLabel.weighty = 1.0;
	gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
	gbc_lblNewLabel.insets = new Insets(3, 7, 5, 5);
	gbc_lblNewLabel.gridx = 1;
	gbc_lblNewLabel.gridy = 2;
	this.add(lblNewLabel, gbc_lblNewLabel);

	tfUser = new JTextField();
	tfUser.setText("");
	GridBagConstraints gbc_tfUSer = new GridBagConstraints();
	gbc_tfUSer.fill = GridBagConstraints.HORIZONTAL;
	gbc_tfUSer.weightx = 0.1;
	gbc_tfUSer.anchor = GridBagConstraints.WEST;
	gbc_tfUSer.insets = new Insets(3, 0, 5, 5);
	gbc_tfUSer.gridx = 2;
	gbc_tfUSer.gridy = 2;
	this.add(tfUser, gbc_tfUSer);
	tfUser.setColumns(10);

	JLabel lblNewLabel_1 = new JLabel("Password:");
	lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
	GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
	gbc_lblNewLabel_1.weighty = 1.0;
	gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
	gbc_lblNewLabel_1.insets = new Insets(3, 7, 5, 5);
	gbc_lblNewLabel_1.gridx = 3;
	gbc_lblNewLabel_1.gridy = 2;
	this.add(lblNewLabel_1, gbc_lblNewLabel_1);

	tfPass = new JTextField();
	GridBagConstraints gbc_tfPass = new GridBagConstraints();
	gbc_tfPass.fill = GridBagConstraints.HORIZONTAL;
	gbc_tfPass.weightx = 0.1;
	gbc_tfPass.insets = new Insets(3, 0, 5, 5);
	gbc_tfPass.anchor = GridBagConstraints.WEST;
	gbc_tfPass.gridx = 4;
	gbc_tfPass.gridy = 2;
	this.add(tfPass, gbc_tfPass);
	tfPass.setColumns(10);

	JButton btnLoadAll = new JButton("Try");
	GridBagConstraints gbc_btnLoadAll = new GridBagConstraints();
	gbc_btnLoadAll.anchor = GridBagConstraints.EAST;
	gbc_btnLoadAll.gridwidth = 2;
	gbc_btnLoadAll.insets = new Insets(1, 7, 5, 5);
	gbc_btnLoadAll.gridx = 3;
	gbc_btnLoadAll.gridy = 0;
	this.add(btnLoadAll, gbc_btnLoadAll);
    }
}
