package uk.ac.soton.mib104.t2.workbench.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.taverna.t2.lang.ui.FileTools;
import net.sf.taverna.t2.lang.ui.KeywordDocument;
import net.sf.taverna.t2.lang.ui.LineEnabledTextPanel;
import net.sf.taverna.t2.lang.ui.LinePainter;
import net.sf.taverna.t2.lang.ui.NoWrapEditorKit;
import net.sf.taverna.t2.workbench.icons.WorkbenchIcons;

import com.famfamfam.Silk;

/**
 * Panel that contains a {@link KeywordDocument} and four buttons: validate, open, save and clear. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 * 
 * @see KeywordDocument
 */
public abstract class KeywordDocumentEditorPane extends JPanel {
	
	private static final Font defaultFont = new Font("Monospaced", Font.PLAIN, 14);

	private static final Color defaultLineColor = new Color(225, 225, 225);

	private static final long serialVersionUID = -8961893184011492131L;

	private final KeywordDocument document;

	private final JEditorPane editorPane;
	
	/**
	 * Empty constructor.
	 * <p>
	 * Equivalent to: <code>KeywordDocumentEditorPane(null);</code>
	 */
	public KeywordDocumentEditorPane() {
		this(null);
	}

	/**
	 * Default constructor.
	 * 
	 * @param t  the initial content of the keyword document
	 */
	public KeywordDocumentEditorPane(final String t) {
		super(new BorderLayout());
		
		final JButton validateButton = new JButton();
		validateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					KeywordDocumentEditorPane.this.doValidate();
					
					JOptionPane.showMessageDialog(KeywordDocumentEditorPane.this, KeywordDocumentEditorPane.this.getValidateResultMessage(true), KeywordDocumentEditorPane.this.getValidateResultTitle(true), JOptionPane.INFORMATION_MESSAGE, WorkbenchIcons.infoMessageIcon);
				} catch (final Throwable ex) {
					JOptionPane.showMessageDialog(KeywordDocumentEditorPane.this, String.format(KeywordDocumentEditorPane.this.getValidateResultMessage(false), ex.getMessage()), KeywordDocumentEditorPane.this.getValidateResultTitle(false), JOptionPane.ERROR_MESSAGE, WorkbenchIcons.errorMessageIcon);
				}
			}
			
		});
		validateButton.setFont(validateButton.getFont().deriveFont(11f));
		validateButton.setIcon(this.getValidateButtonIcon());
		validateButton.setText(this.getValidateButtonText());
		validateButton.setToolTipText(this.getValidateButtonToolTipText());
		
		final JButton openButton = new JButton();
		openButton.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String t = FileTools.readStringFromFile(KeywordDocumentEditorPane.this, KeywordDocumentEditorPane.this.getOpenDialogTitle(), KeywordDocumentEditorPane.this.getFileExtension());
				
				if (t != null) {
					KeywordDocumentEditorPane.this.setText(t);
				}
			}
			
		});
		openButton.setFont(openButton.getFont().deriveFont(11f));
		openButton.setIcon(this.getOpenButtonIcon());
		openButton.setText(this.getOpenButtonText());
		openButton.setToolTipText(this.getOpenButtonToolTipText());
		
		final JButton saveButton = new JButton();
		saveButton.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(final ActionEvent e) {
				FileTools.saveStringToFile(KeywordDocumentEditorPane.this, KeywordDocumentEditorPane.this.getSaveDialogTitle(), KeywordDocumentEditorPane.this.getFileExtension(), KeywordDocumentEditorPane.this.getText());
			}
			
		});
		saveButton.setFont(openButton.getFont().deriveFont(11f));
		saveButton.setIcon(this.getSaveButtonIcon());
		saveButton.setText(this.getSaveButtonText());
		saveButton.setToolTipText(this.getSaveButtonToolTipText());
		
		final JButton clearButton = new JButton();
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (KeywordDocumentEditorPane.this.shouldConfirmClear()) {
					switch (JOptionPane.showConfirmDialog(KeywordDocumentEditorPane.this, KeywordDocumentEditorPane.this.getClearConfirmMessage(), KeywordDocumentEditorPane.this.getClearConfirmTitle(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, WorkbenchIcons.questionMessageIcon)) {
					case JOptionPane.YES_OPTION:
						break;
					default:
						return;
					}
				}
				
				KeywordDocumentEditorPane.this.setText(KeywordDocumentEditorPane.this.getClearText());
			}
			
		});
		clearButton.setFont(clearButton.getFont().deriveFont(11f));
		clearButton.setIcon(this.getClearButtonIcon());
		clearButton.setText(this.getClearButtonText());
		clearButton.setToolTipText(this.getClearButtonToolTipText());
		
		final JPanel buttonPane = new JPanel(new FlowLayout());
		buttonPane.add(validateButton);
		buttonPane.add(openButton);
		buttonPane.add(saveButton);
		buttonPane.add(clearButton);
		
		document = new KeywordDocument(this.getKeys());
		document.addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {
				validateButton.setEnabled(KeywordDocumentEditorPane.this.isValidateButtonEnabled());
				openButton.setEnabled(KeywordDocumentEditorPane.this.isOpenButtonEnabled());
				saveButton.setEnabled(KeywordDocumentEditorPane.this.isSaveButtonEnabled());
				clearButton.setEnabled(KeywordDocumentEditorPane.this.isClearButtonEnabled());
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				this.changedUpdate(e);
			}

			@Override
			public void removeUpdate(final DocumentEvent e) {
				this.changedUpdate(e);
			}
			
		});
		
		editorPane = new JEditorPane();
		new LinePainter(editorPane, defaultLineColor);
		editorPane.setEditorKit(new NoWrapEditorKit());
		editorPane.setFont(defaultFont);
		
		editorPane.setDocument(document);
		editorPane.setText((t == null) ? "" : t);
		editorPane.setCaretPosition(0);
		
		this.add(new LineEnabledTextPanel(editorPane), BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.SOUTH);
		
		validateButton.setEnabled(this.isValidateButtonEnabled());
		openButton.setEnabled(this.isOpenButtonEnabled());
		saveButton.setEnabled(this.isSaveButtonEnabled());
		clearButton.setEnabled(this.isClearButtonEnabled());
	}

	/**
	 * Registers the given observer to begin receiving notifications when changes are made to the document.
	 * 
	 * @param listener  the observer to register
	 */
	public final void addDocumentListener(final DocumentListener listener) {
		document.addDocumentListener(listener);
	}
	
	/**
	 * Adds a port name to the document.
	 * 
	 * @param name  the port name
	 * @see KeywordDocument#addPort(String)
	 */
	public final void addPort(final String name) {
		document.addPort(name);
	}
	
	/**
	 * Called when the "validate" button is clicked. 
	 * 
	 * @throws Throwable
	 */
	protected abstract void doValidate() throws Throwable;

	/**
	 * Returns the icon for the "clear" button.
	 * 
	 * @return  the icon
	 */
	protected Icon getClearButtonIcon() {
		return Silk.getPageWhiteDeleteIcon();
	}
	
	/**
	 * Returns the text for the "clear" button.
	 * 
	 * @return  the text
	 */
	protected String getClearButtonText() {
		return "Clear";
	}
	
	/**
	 * Returns the tooltip text for the "clear" button. 
	 * 
	 * @return  the tooltip text
	 */
	protected String getClearButtonToolTipText() {
		return String.format("Clear %s", this.getKeywordDocumentEditorPaneName());
	}
	
	/**
	 * Returns the confirmation message that is displayed when the "clear" button is clicked.
	 * 
	 * @return  the confirmation message
	 */
	protected String getClearConfirmMessage() {
		return String.format("<html><body width=\"380\">You have edited the %s. If you continue, your changes will be lost. Do you want to continue anyway?</body></html>", this.getKeywordDocumentEditorPaneName());
	}

	/**
	 * Returns the confirmation title that is displayed when the "clear" button is clicked.
	 * 
	 * @return  the confirmation title
	 */
	protected String getClearConfirmTitle() {
		return "Are you sure?";
	}
	
	/**
	 * Returns the text that replaces the content of the document when the "clear" button is clicked.
	 * 
	 * @return  the text
	 */
	protected String getClearText() {
		return "";
	}
	
	/**
	 * Returns the extension for files that may be edited using this panel.
	 * 
	 * @return  the file extension
	 */
	protected abstract String getFileExtension();
	
	/**
	 * Returns the set of keywords for the document.
	 * 
	 * @return  the set of keywords
	 * @see KeywordDocument#KeywordDocument(Set)
	 */
	protected abstract Set<String> getKeys();

	/**
	 * Returns the name of this editor pane; used to construct text and tooltip text for buttons.
	 * 
	 * @return  the name of this editor pane
	 */
	protected abstract String getKeywordDocumentEditorPaneName();
	
	/**
	 * Returns the icon for the "open" button.
	 * 
	 * @return  the icon
	 */
	protected Icon getOpenButtonIcon() {
		return WorkbenchIcons.openIcon;
	}
	
	/**
	 * Returns the text for the "open" button.
	 * 
	 * @return  the text
	 */
	protected String getOpenButtonText() {
		return "Open";
	}
	
	/**
	 * Returns the tooltip text for the "open" button.
	 * 
	 * @return  the tooltip text
	 */
	protected String getOpenButtonToolTipText() {
		return String.format("Replace %s with contents of *.%s file", this.getKeywordDocumentEditorPaneName(), this.getFileExtension());
	}
	
	/**
	 * Returns the title for the file selection dialog that is displayed when the "open" button is clicked. 
	 * 
	 * @return  the dialog title
	 */
	protected String getOpenDialogTitle() {
		return String.format("Open %s", this.getKeywordDocumentEditorPaneName());
	}
	
	/**
	 * Returns the icon for the "save" button.
	 * 
	 * @return  the icon
	 */
	protected Icon getSaveButtonIcon() {
		return WorkbenchIcons.saveIcon;
	}
	
	/**
	 * Returns the text for the "save" button.
	 * 
	 * @return  the text
	 */
	protected String getSaveButtonText() {
		return "Save";
	}
	
	/**
	 * Returns the tooltip text for the "save" button.
	 * 
	 * @return  the tooltipt text
	 */
	protected String getSaveButtonToolTipText() {
		return String.format("Write %s to *.%s file", this.getKeywordDocumentEditorPaneName(), this.getFileExtension());
	}
	
	/**
	 * Returns the title for the file selection dialog that is displayed when the "save" button is clicked.
	 * 
	 * @return  the dialog title
	 */
	protected String getSaveDialogTitle() {
		return String.format("Save %s", this.getKeywordDocumentEditorPaneName());
	}
	
	/**
	 * Returns the text contained in the document.
	 * 
	 * @return  the text contained in the document
	 */
	public final String getText() {
		return editorPane.getText();
	}

	/**
	 * Returns the icon for the "validate" button.
	 * 
	 * @return  the icon
	 */
	protected Icon getValidateButtonIcon() {
		return Silk.getMagnifierIcon();
	}
	
	/**
	 * Returns the text for the "validate" button.
	 * 
	 * @return  the text
	 */
	protected String getValidateButtonText() {
		return "Validate";
	}
	
	/**
	 * Returns the tooltip text for the "validate" button.
	 * 
	 * @return  the tooltip text
	 */
	protected String getValidateButtonToolTipText() {
		return String.format("Validate %s", this.getKeywordDocumentEditorPaneName());
	}
	
	/**
	 * Returns the message that is displayed when the "validate" button is clicked.
	 * 
	 * @param valid  <code>true</code> if the validation was successful, otherwise, <code>false</code>
	 * @return  the message
	 */
	protected String getValidateResultMessage(boolean valid) {
		if (valid) {
			return String.format("%s is OK", this.getKeywordDocumentEditorPaneName());
		} else {
			return String.format("Invalid %s: %s", this.getKeywordDocumentEditorPaneName(), "%s");
		}
	}
	
	/**
	 * Returns the title that is displayed when the "validate" button is clicked. 
	 * 
	 * @param valid  <code>true</code> if the validation was successful, otherwise, <code>false</code>
	 * @return  the title
	 */
	protected String getValidateResultTitle(boolean valid) {
		return "Health Check Report";
	}
	
	/**
	 * Returns <code>true</code> if the "clear" button should be enabled.
	 * 
	 * @return  <code>true</code> if the "clear" button should be enabled.
	 */
	protected boolean isClearButtonEnabled() {
		return !this.getText().trim().isEmpty();
	}
	
	/**
	 * Returns <code>true</code> if the "open" button should be enabled.
	 * 
	 * @return  <code>true</code> if the "open" button should be enabled.
	 */
	protected boolean isOpenButtonEnabled() {
		return true;
	}
	
	/**
	 * Returns <code>true</code> if the "save" button should be enabled.
	 * 
	 * @return  <code>true</code> if the "save" button should be enabled.
	 */
	protected boolean isSaveButtonEnabled() {
		return true;
	}
	
	/**
	 * Returns <code>true</code> if the "validate" button should be enabled.
	 * 
	 * @return  <code>true</code> if the "validate" button should be enabled.
	 */
	protected boolean isValidateButtonEnabled() {
		return true;
	}
	
	/**
	 * Unregisters the given observer from the notification list so it will no longer receive change updates.
	 * 
	 * @param listener  the observer to unregister
	 */
	public final void removeDocumentListener(final DocumentListener listener) {
		document.removeDocumentListener(listener);
	}
	
	/**
	 * Removes a port name from the document.
	 * 
	 * @param name  the port name
	 * @see KeywordDocument#removePort(String)
	 */
	public final void removePort(final String name) {
		document.removePort(name);
	}
	
	/**
	 * Sets the text contained in the document.
	 * 
	 * @param t  the new text
	 */
	public final void setText(final String t) {
		editorPane.setText(t);
		editorPane.setCaretPosition(0);
	}
	
	/**
	 * Returns <code>true</code> if the confirmation dialog should be displayed when the "clear" button is clicked. 
	 * 
	 * @return  <code>true</code> if the confirmation dialog should be displayed when the "clear" button is clicked. 
	 */
	protected boolean shouldConfirmClear() {
		return !this.getText().trim().isEmpty();
	}
	
}
