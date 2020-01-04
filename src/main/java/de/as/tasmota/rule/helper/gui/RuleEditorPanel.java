package de.as.tasmota.rule.helper.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import de.as.tasmota.rule.helper.controller.RuleEditorController;
import de.as.tasmota.rule.helper.eventbus.BusEvent;
import de.as.tasmota.rule.helper.eventbus.EventRegistry;
import de.as.tasmota.rule.helper.eventbus.Registration;
import de.as.tasmota.rule.helper.model.RuleEditorModel;

public class RuleEditorPanel extends JPanel {

    private static final long serialVersionUID = 2140071908788902424L;

    private static final EventRegistry<String, BusEvent> EVENT_BUS = EventRegistry.instance();

    public Registration register(String key, Consumer<BusEvent> listener) {
	return EVENT_BUS.register(key, listener);
    }

    private List<Registration> registrations = new ArrayList<>();

    public void release() {
	this.registrations.forEach(Registration::remove);
    }

    private JTextPane taCodeEditor;

    private Document editorPaneDocument;
    protected UndoHandler undoHandler = new UndoHandler();
    protected UndoManager undoManager = new UndoManager();
    private UndoAction undoAction = null;
    private RedoAction redoAction = null;

    private void initModel(RuleEditorModel model) {
	this.registrations.add(model.registerStringBridge(RuleEditorModel.KEY_RULEEDITOR_TEXT,
		() -> taCodeEditor.getText(), (v) -> taCodeEditor.setText(v)));
    }

//    /**
//     * Nur für den WindowBuilder.
//     */
//    public RuleEditorPanel() {
//	this(null, null);
//    }

    /**
     * Create the panel.
     */
    public RuleEditorPanel(RuleEditorController controller) {
//	this.controller = controller;
	setLayout(new BorderLayout(0, 0));
	initialize(controller);
	initModel(controller.getModel());

	// undo and redo (s.https://alvinalexander.com/java/java-undo-redo)
	editorPaneDocument = taCodeEditor.getDocument();
	editorPaneDocument.addUndoableEditListener(undoHandler);
	editorPaneDocument.addDocumentListener(undoHandler);

	KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
	KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);

//	CompoundUndoManager um = new CompoundUndoManager(this.taCodeEditor);
//	taCodeEditor.getInputMap().put(undoKeystroke, "undoKeystroke");
//	taCodeEditor.getActionMap().put("undoKeystroke", um.getUndoAction());
//	
//	redoAction = new RedoAction();
//	taCodeEditor.getInputMap().put(redoKeystroke, "redoKeystroke");
//	taCodeEditor.getActionMap().put("redoKeystroke", um.getRedoAction());

	undoAction = new UndoAction();
	taCodeEditor.getInputMap().put(undoKeystroke, "undoKeystroke");
	taCodeEditor.getActionMap().put("undoKeystroke", undoAction);

	redoAction = new RedoAction();
	taCodeEditor.getInputMap().put(redoKeystroke, "redoKeystroke");
	taCodeEditor.getActionMap().put("redoKeystroke", redoAction);

	// Grab focus
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		taCodeEditor.requestFocusInWindow();
	    }
	});

//	controller.getModel().addBridge(RuleEditorModel.KEY_RULEEDITOR_TEXT, () -> taCodeEditor.getText(), (v) -> taCodeEditor.setText(v));
//	controller.getModel().addBridge(RuleEditorModel.KEY_RULEEDITOR_TEXT, new ValueBridge<String>() {
//
//	    @Override
//	    public String getValue() {
//		return taCodeEditor.getText();
//	    }
//
//	    @Override
//	    public void setValue(String value) {
//		taCodeEditor.setText(value);
//	    }
//	});
    }

    private void initialize(RuleEditorController controller) {
	JScrollPane spCe = new JScrollPane();
	this.add(spCe, BorderLayout.CENTER);

	this.taCodeEditor = new RuleEditorPane();
//	((RuleDocument) this.taCodeEditor.getDocument()).undoHandler = this.undoHandler;
	this.taCodeEditor.setBorder(BorderFactory.createEtchedBorder());
	spCe.setViewportView(this.taCodeEditor);

	JPanel pCe = new JPanel();
	pCe.setBorder(BorderFactory.createEtchedBorder());
	spCe.setColumnHeaderView(pCe);
	GridBagLayout gbl_pCe = new GridBagLayout();
	gbl_pCe.columnWidths = new int[] { 0, 0, 0, 0, 0 };
	gbl_pCe.rowHeights = new int[] { 22, 0, 0 };
	gbl_pCe.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
	gbl_pCe.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
	pCe.setLayout(gbl_pCe);

	JLabel label = new JLabel("Rule Editor");
	label.setFont(new Font("Tahoma", Font.BOLD, 11));
	pCe.add(label, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
		GridBagConstraints.HORIZONTAL, new Insets(3, 7, 5, 7), 0, 0));

	JButton bCompile = new JButton("Pack >>");
	bCompile.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
//		RulePacker packer = new RulePacker();
//		String text = taCodeEditor.getText();
//		transformEvent.textReceived(text);
		controller.actionPackCode();
		taCodeEditor.requestFocusInWindow();
//		List<String> rpacked = packer.pack(text);
		// TODO
		// taCc1.setText(rpacked.get(0));
	    }
	});
	pCe.add(bCompile, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
		new Insets(3, 3, 5, 3), 0, 0));

	JButton bFormat = new JButton("Format");
	bFormat.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		int pos = taCodeEditor.getCaretPosition();
//		RuleScript script = RuleParser.parse(controller.getModel().getEditorText());
//		controller.getModel().setEditorText(script.writeFormated() + script.getUnparsedText());
		controller.actionFormatCode();
		taCodeEditor.setCaretPosition(Math.min(pos, taCodeEditor.getCaretPosition()));
		taCodeEditor.requestFocusInWindow();
	    }
	});
	GridBagConstraints gbc_bFormat = new GridBagConstraints();
	gbc_bFormat.anchor = GridBagConstraints.EAST;
	gbc_bFormat.insets = new Insets(3, 3, 5, 5);
	gbc_bFormat.gridx = 3;
	gbc_bFormat.gridy = 0;
	pCe.add(bFormat, gbc_bFormat);

	JButton bLoad = new JButton("Load");
	bLoad.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// TODO
		controller.actionLoad();
	    }
	});
	GridBagConstraints gbc_bLoad = new GridBagConstraints();
	gbc_bLoad.insets = new Insets(3, 3, 3, 3);
	gbc_bLoad.gridx = 1;
	gbc_bLoad.gridy = 0;
	pCe.add(bLoad, gbc_bLoad);

	JButton bSave = new JButton("Save");
	bSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// TODO
		controller.actionSave();

	    }
	});
	GridBagConstraints gbc_bSave = new GridBagConstraints();
	gbc_bSave.insets = new Insets(3, 3, 3, 3);
	gbc_bSave.gridx = 2;
	gbc_bSave.gridy = 0;
	pCe.add(bSave, gbc_bSave);
    }

    // java undo and redo action classes

    class UndoHandler implements UndoableEditListener, DocumentListener {

	// important, because the undoableEditListener should not react to
	// eventType == CHANGE which is for example invoked on syntax highlighting
	EventType eventType;

	public void changedUpdate(DocumentEvent e) {
	    eventType = e.getType();
	}

	public void removeUpdate(DocumentEvent e) {
	    eventType = e.getType();
	}

	public void insertUpdate(DocumentEvent e) {
	    eventType = e.getType();
	}

	private boolean enabled = true;

	public void setEnabled(boolean enabled) {
	    this.enabled = enabled;
	}

	/**
	 * Messaged when the Document has created an edit, the edit is added to
	 * <code>undoManager</code>, an instance of UndoManager.
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
	    if (eventType == DocumentEvent.EventType.CHANGE) {
		return;
	    }

//            if (eventConsumed)
//                return;
//	    if (!"style change".equals(e.getEdit().getPresentationName())) { TODO
//	    if (this.enabled) {
	    undoManager.addEdit(e.getEdit());

	    undoAction.update();
	    redoAction.update();

//	    // TODO: Test only! Bessere Stelle finden!
//	    try {
//		DefaultStyledDocument doc = (DefaultStyledDocument)taCodeEditor.getDocument();
//		(new RuleDocumentAtributesProcessor()).applyStyleAttributes(doc, 0, taCodeEditor.getText());
//	    } catch (BadLocationException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	    }
//	    }
	}
    }

    class UndoAction extends AbstractAction {

	private static final long serialVersionUID = 2961228002168799581L;

	public UndoAction() {
	    super("Undo");
	    setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
	    try {
		undoManager.undo();
	    } catch (CannotUndoException ex) {
		// TODO deal with this
		ex.printStackTrace();
	    }
	    update();
	    redoAction.update();
	}

	protected void update() {
	    if (undoManager.canUndo()) {
		setEnabled(true);
		putValue(Action.NAME, undoManager.getUndoPresentationName());
	    } else {
		setEnabled(false);
		putValue(Action.NAME, "Undo");
	    }
	}
    }

    class RedoAction extends AbstractAction {

	private static final long serialVersionUID = -7402186813147131445L;

	public RedoAction() {
	    super("Redo");
	    setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
	    try {
		undoManager.redo();
	    } catch (CannotRedoException ex) {
		// TODO deal with this
		ex.printStackTrace();
	    }
	    update();
	    undoAction.update();
	}

	protected void update() {
	    if (undoManager.canRedo()) {
		setEnabled(true);
		putValue(Action.NAME, undoManager.getRedoPresentationName());
	    } else {
		setEnabled(false);
		putValue(Action.NAME, "Redo");
	    }
	}
    }
}
