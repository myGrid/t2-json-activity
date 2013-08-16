package uk.ac.soton.mib104.t2.workbench.ui.jsonpath;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.text.Document;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeSelectionModel;

import com.famfamfam.Silk;

import com.jayway.jsonpath.JsonPath;

import uk.ac.soton.mib104.t2.workbench.ui.TreeUtils;
import uk.ac.soton.mib104.t2.workbench.ui.json.tree.JSONTree;
import uk.ac.soton.mib104.t2.workbench.ui.json.tree.JSONTreeNode;

public final class JSONPathTextField extends JPanel {
	
	private static final URI documentationURI = URI.create("http://goessner.net/articles/JsonPath/");
	
	private static final long serialVersionUID = -3584381689271249537L;
	
	private static final String textFieldHintEmpty = "There is no JSONPath expression to validate";
	
	private static final String textFieldHintInvalidFormat1 = "Invalid JSONPath expression: %s";
	
	private static final String textFieldHintValid = "JSONPath expression is well-formed and valid";
	
	private static final Icon textFieldIconEmpty = Silk.getHelpIcon();
	
	private static final Icon textFieldIconInvalid = Silk.getExclamationIcon();
	
	private static final Icon textFieldIconValid = Silk.getAcceptIcon();
	
	private static final String textFieldText = "JSONPath:";
	
	private static final String treeText = "No JSON tree";
	
	public static final JLabel createLabelForDocument(final Document document) {
		if (document == null) {
			throw new IllegalArgumentException("document");
		}
		
		final JLabel label = new JLabel();
		label.setFont(label.getFont().deriveFont(11f));
		label.setText(textFieldText);
		
		final DocumentListener listener = new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {
				try {
					final String t = document.getText(0, document.getLength());
					
					if (t.trim().isEmpty()) {
						label.setIcon(textFieldIconEmpty);
						label.setToolTipText(textFieldHintEmpty);
					} else {
						@SuppressWarnings("unused")
						final JsonPath jsonPath = JsonPath.compile(t);
						
						label.setIcon(textFieldIconValid);
						label.setToolTipText(textFieldHintValid);
					}
				} catch (final Throwable ex) {
					label.setIcon(textFieldIconInvalid);
					label.setToolTipText(String.format(textFieldHintInvalidFormat1, ex.getMessage()));
				}
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				this.changedUpdate(e);
			}

			@Override
			public void removeUpdate(final DocumentEvent e) {
				this.changedUpdate(e);
			}
			
		};
		
		document.addDocumentListener(listener);
		
		listener.changedUpdate(null);
		
		return label;
	}
	
	public static final JLabel createLabelForDocumentationURI() {
		final JLabel label = new JLabel();
		label.setFont(label.getFont().deriveFont(11f));
		label.setHorizontalAlignment(JLabel.CENTER);
		
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			label.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(final MouseEvent e) {
					try {
						Desktop.getDesktop().browse(documentationURI);
					} catch (final Throwable ex) {
						return;
					}
				}
				
			});
			label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			label.setText(String.format("<html><a href=\"%s\">%s</a></html>", documentationURI.toASCIIString(), documentationURI.toASCIIString()));
			label.setToolTipText(documentationURI.toASCIIString());
		} else {
			label.setEnabled(false);
			label.setText(documentationURI.toASCIIString());
		}
		
		return label;
	}
	
	private volatile boolean shouldExpandTree = false;
	
	private volatile boolean shouldUpdateTextField = true;
	
	private final JTextField textField = new JTextField();
	
	private final JSONTree tree = new JSONTree();
	
	private final JLabel treeEmptyLabel = new JLabel();
	
	private final JScrollPane treeScrollPane = new JScrollPane(tree);
	
	private boolean treeVisible = false;
	
	public JSONPathTextField() {
		super(new GridBagLayout());
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				if (shouldUpdateTextField) {
					final JsonPath jsonPath = JSONTreeNode.compile(e.getNewLeadSelectionPath());
					
					if (jsonPath != null) {
						textField.setText(jsonPath.getPath());
					}
				}
			}
			
		});
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {

			@Override
			public void treeWillCollapse(final TreeExpansionEvent e) throws ExpandVetoException {
				if (!shouldExpandTree) {
					throw new ExpandVetoException(e);
				}
			}

			@Override
			public void treeWillExpand(final TreeExpansionEvent e) throws ExpandVetoException {
				if (!shouldExpandTree) {
					throw new ExpandVetoException(e);
				}
			}
			
		});
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
		treeEmptyLabel.setHorizontalAlignment(JLabel.CENTER);
		treeEmptyLabel.setText(treeText);
		treeEmptyLabel.setVerticalAlignment(JLabel.CENTER);
		
		textField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void changedUpdate(final DocumentEvent e) {
				JSONPathTextField.this.changedTextField();
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

		final GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 2;
		constraints.weightx = 1f;
		constraints.weighty = 1f;
		this.add(treeEmptyLabel, constraints);
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		this.add(createLabelForDocument(textField.getDocument()), constraints);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 1f;
		constraints.weighty = 0;
		this.add(textField, constraints);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 1f;
		constraints.weighty = 0;
		this.add(createLabelForDocumentationURI(), constraints);
	}
	
	public final void addDocumentListener(final DocumentListener listener) {
		textField.getDocument().addDocumentListener(listener);
	}
	
	private synchronized void changedTextField() {
		final boolean b = shouldUpdateTextField;
		
		shouldUpdateTextField = false;
		
		try {
			final String t = textField.getText();
			
			if (t.trim().isEmpty()) {
				tree.clearSelection();
			} else {
				try {
					final JsonPath jsonPath = JsonPath.compile(t);
					
					this.expandOrCollapseTree(true);
					
					final int[] rows = tree.getRows(jsonPath);
					
					if ((rows == null) || (rows.length == 0)) {
						tree.clearSelection();
					} else {
						tree.setSelectionRows(rows);
					}
				} catch (final Throwable ex) {
					tree.clearSelection();
				}
			}
		} finally {
			shouldUpdateTextField = b;
		}
	}
	
	private synchronized void expandOrCollapseTree(final boolean expand) {
		final boolean b = shouldExpandTree; 
		
		shouldExpandTree = true;
		
		try {
			TreeUtils.expandOrCollapseAllRows(tree, expand);
		} finally {
			shouldExpandTree = b;
		}
	}
	
	public final Object getJSONValue() {
		return tree.getJSONValue();
	}
	
	public final String getText() {
		return textField.getText();
	}
	
	public final boolean isTreeVisible() {
		return treeVisible;
	}
	
	public final void removeDocumentListener(final DocumentListener listener) {
		textField.getDocument().removeDocumentListener(listener);
	}
	
	public final void setJSONValue(final Object value) {
		tree.setJSONValue(value);
	}
	
	public final void setText(final String t) {
		textField.setText(t);
	}
	
	public final void setTreeVisible(final boolean treeVisible) {
		final boolean propertyChanged = this.treeVisible != treeVisible;
		
		if (propertyChanged) {
			this.remove(treeVisible ? treeEmptyLabel : treeScrollPane);
			
			final GridBagConstraints constraints = new GridBagConstraints();
			
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridheight = 1;
			constraints.gridwidth = 2;
			constraints.weightx = 1f;
			constraints.weighty = 1f;
			this.add(treeVisible ? treeScrollPane : treeEmptyLabel, constraints);
			
			this.revalidate();
			this.repaint();
		}
		
		this.treeVisible = treeVisible;
		
		if (treeVisible) {
			this.expandOrCollapseTree(true);
			
			this.changedTextField();
		}
		
		if (propertyChanged) {
			this.firePropertyChange("treeVisible", !treeVisible, treeVisible);
		}
	}

}
