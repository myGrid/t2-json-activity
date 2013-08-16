package uk.ac.soton.mib104.t2.workbench.ui.jsonpath;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.taverna.t2.workbench.icons.WorkbenchIcons;

import uk.ac.soton.mib104.t2.activities.json.JSONPathUtils;
import uk.ac.soton.mib104.t2.activities.json.JSONStringUtils;
import uk.ac.soton.mib104.t2.workbench.ui.json.text.JSONKeywordDocumentEditorPane;

import com.famfamfam.Silk;

import com.jayway.jsonpath.spi.JsonProvider;

public final class JSONPathInputPanel extends JPanel {
	
	private static final Icon clearIcon = Silk.getChartOrganisationDeleteIcon();

	private static final String clearText = "Clear";
	
	private static final String clearTip = "Clear JSON tree (right)";
	
	private static final Dimension defaultSize = new Dimension(320, 240);
	
	private static final Icon exportIcon = Silk.getArrowLeftIcon();
	
	private static final String exportMessage = "<html><body width=\"380\">You have edited the JSON document. If you continue, your changes will be lost. Do want to continue anyway?</body></html>";
	
	private static final String exportText = "Export";
	
	private static final String exportTip = "Generate JSON document (left) using JSON tree (right)";
	
	private static final String exportTitle = "Are you sure?";
	
	private static final String importExceptionInvalidClassFormat1 = "Expected: JSON array or object. Received: %s";
	
	private static final String importExceptionInvalidDocumentMessageFormat1 = "Invalid JSON document (for JSONPath): %s";
	
	private static final String importExceptionInvalidDocumentTitle = "Invalid JSON Document (for JSONPath)";
	
	private static final String importExceptionNull = "Expected: JSON array or object. Received: null";
	
	private static final Icon importIcon = Silk.getArrowRightIcon();
	
	private static final String importMessage = "<html><body width=\"380\">You have edited the JSON tree. If you continue, your changes will be lost. Do you want to continue anyway?</body></html>";
	
	private static final String importText = "Import";
	
	private static final String importTip = "Generate JSON tree (right) using JSON document (left)";
	
	private static final String importTitle = "Are you sure?";
	
	private static final long serialVersionUID = 45987014384540898L;
	
	private final JSONKeywordDocumentEditorPane jsonDocumentEditorPane = new JSONKeywordDocumentEditorPane();
	
	private final JSONPathTextField jsonPathEditorPane = new JSONPathTextField();
	
	public JSONPathInputPanel() {
		super(new GridBagLayout());
		
		final JButton importButton = new JButton();
		importButton.addActionListener(new ActionListener() {
			
			private final JsonProvider provider = JSONPathUtils.createProvider();
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String t = jsonDocumentEditorPane.getText();
				
				try {
					final Object jsonValue = JSONStringUtils.parseJSONString(t);
					
					if (jsonValue == null) {
						throw new IllegalArgumentException(importExceptionNull);
					} else if (provider.isContainer(jsonValue)) {
						if (jsonPathEditorPane.isTreeVisible()) {
							switch (JOptionPane.showConfirmDialog(JSONPathInputPanel.this, importMessage, importTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, WorkbenchIcons.questionMessageIcon)) {
							case JOptionPane.YES_OPTION:
								break;
							default:
								return;
							}
						}
						
						jsonPathEditorPane.setJSONValue(jsonValue);
						
						jsonPathEditorPane.setTreeVisible(true);
					} else {
						throw new IllegalArgumentException(String.format(importExceptionInvalidClassFormat1, jsonValue.getClass().getName()));
					}
				} catch (final Throwable ex) {
					JOptionPane.showMessageDialog(JSONPathInputPanel.this, String.format(importExceptionInvalidDocumentMessageFormat1, ex.getMessage()), importExceptionInvalidDocumentTitle, JOptionPane.ERROR_MESSAGE, WorkbenchIcons.errorMessageIcon);
				}
			}
			
		});
		importButton.setEnabled(false);
		importButton.setFont(importButton.getFont().deriveFont(11f));
		importButton.setIcon(importIcon);
		importButton.setText(importText);
		importButton.setToolTipText(importTip);
		
		final JButton exportButton = new JButton();
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!jsonDocumentEditorPane.getText().trim().isEmpty()) {
					switch (JOptionPane.showConfirmDialog(JSONPathInputPanel.this, exportMessage, exportTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, WorkbenchIcons.questionMessageIcon)) {
					case JOptionPane.YES_OPTION:
						break;
					default:
						return;
					}
				}
				
				final String jsonString = JSONStringUtils.toJSONString(jsonPathEditorPane.getJSONValue(), true);
				
				jsonDocumentEditorPane.setText(jsonString);
			}
			
		});
		exportButton.setEnabled(false);
		exportButton.setFont(exportButton.getFont().deriveFont(11f));
		exportButton.setIcon(exportIcon);
		exportButton.setText(exportText);
		exportButton.setToolTipText(exportTip);
		
		final JButton clearButton = new JButton();
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (jsonPathEditorPane.isTreeVisible()) {
					switch (JOptionPane.showConfirmDialog(JSONPathInputPanel.this, importMessage, importTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, WorkbenchIcons.questionMessageIcon)) {
					case JOptionPane.YES_OPTION:
						break;
					default:
						return;
					}
					
					jsonPathEditorPane.setTreeVisible(false);
				}
			}
			
		});
		clearButton.setEnabled(false);
		clearButton.setFont(clearButton.getFont().deriveFont(11f));
		clearButton.setIcon(clearIcon);
		clearButton.setText(clearText);
		clearButton.setToolTipText(clearTip);
		
		jsonDocumentEditorPane.addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {
				final boolean enabled = !jsonDocumentEditorPane.getText().trim().isEmpty();
				
				importButton.setEnabled(enabled);
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
		jsonDocumentEditorPane.setMinimumSize(defaultSize);
		jsonDocumentEditorPane.setPreferredSize(jsonDocumentEditorPane.getMinimumSize());
		
		jsonPathEditorPane.addPropertyChangeListener("treeVisible", new PropertyChangeListener() {

			@Override
			public void propertyChange(final PropertyChangeEvent e) {
				final boolean treeVisible = (Boolean) e.getNewValue();
				
				exportButton.setEnabled(treeVisible);
				clearButton.setEnabled(treeVisible);
			}
			
		});
		jsonPathEditorPane.setMinimumSize(defaultSize);
		jsonPathEditorPane.setPreferredSize(jsonPathEditorPane.getMinimumSize());
		
		final JPanel buttonsPane = new JPanel(new GridLayout(3, 1));
		buttonsPane.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		buttonsPane.add(importButton);
		buttonsPane.add(exportButton);
		buttonsPane.add(clearButton);
		
		{
			final GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.weightx = 1f;
			constraints.weighty = 1f;
			this.add(jsonDocumentEditorPane, constraints);
			constraints.fill = GridBagConstraints.NONE;
			constraints.gridx++;
			constraints.weightx = 0f;
			constraints.weighty = 0f;
			this.add(buttonsPane, constraints);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx++;
			constraints.weightx = 1f;
			constraints.weighty = 1f;
			this.add(jsonPathEditorPane, constraints);
		}
	}
	
	public final JSONKeywordDocumentEditorPane getJSONDocumentEditorPane() {
		return jsonDocumentEditorPane;
	}
	
	public final JSONPathTextField getJsonPathEditorPane() {
		return jsonPathEditorPane;
	}

}
