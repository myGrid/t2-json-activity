package uk.ac.soton.mib104.t2.activities.json.ui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

import org.apache.commons.lang.ObjectUtils;

import uk.ac.soton.mib104.t2.activities.json.JSONPathActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONPathActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceIcon;
import uk.ac.soton.mib104.t2.workbench.ui.PortDepthInputPanel;
import uk.ac.soton.mib104.t2.workbench.ui.jsonpath.JSONPathInputDialog;
import uk.ac.soton.mib104.t2.workbench.ui.jsonpath.JSONPathInputPanel;
import uk.ac.soton.mib104.t2.workbench.ui.jsonpath.JSONPathTextField;

import com.famfamfam.Silk;

public final class JSONPathConfigurationPanel extends ActivityConfigurationPanel<JSONPathActivity, JSONPathActivityConfigurationBean> {

	private static final String jsonPathButtonText = "Select JSONPath";
	
	private static final String jsonPathButtonTip = "Launch JSONPath expression editor";
	
	private static final String portDepthInputPaneText = "Result type:";
	
	private static final String portDepthInputPaneTip = "The granularity of the result of the evaluation of the JSONPath expression: single value (0), list of values (1), list of lists (2), etc.";
	
	private static final long serialVersionUID = 7436658580823521770L;
	
	private final JSONPathActivity activity;
	
	private JSONPathActivityConfigurationBean configBean;
	
	private final JTextField jsonPathAsTextField = new JTextField();
	
	private final JButton jsonPathButton = new JButton();
	
	private String jsonPathInputDialog_jsonPathEditorPane_text;
	
	private boolean jsonPathInputDialog_jsonPathEditorPane_treeVisible;
	
	private Object jsonPathInputDialog_jsonPathEditorPane_value;
	
	private final PortDepthInputPanel portDepthInputPane = new PortDepthInputPanel();

	public JSONPathConfigurationPanel(final JSONPathActivity activity) {
		super();
		
		if (activity == null) {
			throw new IllegalArgumentException(new NullPointerException("activity"));
		}
		
		this.activity = activity;
		
		this.initGui();
		this.refreshConfiguration();
	}
	
	@Override
	public boolean checkValues() {
		return true;
	}
	
	public JSONPathActivity getActivity() {
		if (activity == null) {
			throw new IllegalStateException(new NullPointerException("activity"));
		}
		
		return activity;
	}

	@Override
	public JSONPathActivityConfigurationBean getConfiguration() {
		return configBean;
	}

	protected void initGui() {
		this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.setLayout(new GridBagLayout());

		jsonPathAsTextField.setMinimumSize(new Dimension(240, jsonPathAsTextField.getMinimumSize().height));
		jsonPathAsTextField.setPreferredSize(jsonPathAsTextField.getMinimumSize());
		jsonPathAsTextField.setText("");
		
		jsonPathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JSONPathInputDialog jsonPathInputDialog = new JSONPathInputDialog(SwingUtilities.getWindowAncestor(JSONPathConfigurationPanel.this));
				
				final JSONPathInputPanel jsonPathInputPane = jsonPathInputDialog.getJSONPathInputPane();
				
				jsonPathInputPane.getJSONDocumentEditorPane().setText(jsonPathInputDialog_jsonPathEditorPane_text);
				
				jsonPathInputPane.getJsonPathEditorPane().setJSONValue(jsonPathInputDialog_jsonPathEditorPane_value);
				jsonPathInputPane.getJsonPathEditorPane().setTreeVisible(jsonPathInputDialog_jsonPathEditorPane_treeVisible);
				jsonPathInputPane.getJsonPathEditorPane().setText(jsonPathAsTextField.getText());
				
				jsonPathInputDialog.setVisible(true);
				
				switch (jsonPathInputDialog.getOption()) {
				case JOptionPane.OK_OPTION:
					break;
				default:
					return;
				}
				
				jsonPathInputDialog_jsonPathEditorPane_text = jsonPathInputPane.getJSONDocumentEditorPane().getText();
				jsonPathInputDialog_jsonPathEditorPane_treeVisible = jsonPathInputPane.getJsonPathEditorPane().isTreeVisible();
				jsonPathInputDialog_jsonPathEditorPane_value = jsonPathInputPane.getJsonPathEditorPane().getJSONValue();
				
				jsonPathAsTextField.setText(jsonPathInputPane.getJsonPathEditorPane().getText());
			}
			
		});
		jsonPathButton.setFont(jsonPathButton.getFont().deriveFont(11f));
		jsonPathButton.setIcon(JSONPathServiceIcon.getIcon());
		jsonPathButton.setText(jsonPathButtonText);
		jsonPathButton.setToolTipText(jsonPathButtonTip);
		
		final JLabel portDepthLabel = new JLabel();
		portDepthLabel.setFont(portDepthLabel.getFont().deriveFont(11f));
		portDepthLabel.setHorizontalAlignment(JLabel.LEFT);
		portDepthLabel.setIcon(Silk.getHelpIcon());
		portDepthLabel.setText(portDepthInputPaneText);
		portDepthLabel.setToolTipText(portDepthInputPaneTip);
		
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		constraints.anchor = GridBagConstraints.WEST;
		this.add(JSONPathTextField.createLabelForDocument(jsonPathAsTextField.getDocument()), constraints);
		
		constraints.gridx++;
		
		constraints.anchor = GridBagConstraints.EAST;
		constraints.weightx = 1d;
		this.add(jsonPathAsTextField, constraints);
		constraints.weightx = 0;
		
		constraints.gridx++;
		
		constraints.fill = GridBagConstraints.NONE;
		this.add(jsonPathButton, constraints);
		
		constraints.gridx--;
		
		constraints.gridy++;
		
		constraints.anchor = GridBagConstraints.CENTER;
		this.add(JSONPathTextField.createLabelForDocumentationURI(), constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		constraints.gridx = 0;
		constraints.gridy++;
		
		constraints.gridwidth = 3;
		this.add(new JSeparator(JSeparator.HORIZONTAL), constraints);
		constraints.gridwidth = 1;
		
		constraints.gridx = 0;
		constraints.gridy++;
		
		constraints.anchor = GridBagConstraints.WEST;
		this.add(portDepthLabel, constraints);
		
		constraints.gridx++;
		
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 2;
		this.add(portDepthInputPane, constraints);
		constraints.gridwidth = 1;
	}
	
	@Override
	public boolean isConfigurationChanged() {
		return !ObjectUtils.equals(this.getConfiguration(), this.toConfiguration());
	}

	@Override
	public void noteConfiguration() {
		configBean = this.toConfiguration();
	}
	
	@Override
	public void refreshConfiguration() {
		configBean = this.getActivity().getConfiguration();
		
		portDepthInputPane.setPortDepth(configBean.getDepth());
		jsonPathInputDialog_jsonPathEditorPane_text = configBean.getJsonString();
		jsonPathAsTextField.setText(configBean.getJsonPathString());
		jsonPathInputDialog_jsonPathEditorPane_value = configBean.getJsonTreeValue();
		jsonPathInputDialog_jsonPathEditorPane_treeVisible = configBean.isJsonTreeVisible();
 	}
	
	public JSONPathActivityConfigurationBean toConfiguration() {
		final JSONPathActivityConfigurationBean configBean = new JSONPathActivityConfigurationBean();
		
		configBean.setDepth(portDepthInputPane.getPortDepth());
		configBean.setJsonString(jsonPathInputDialog_jsonPathEditorPane_text);
		configBean.setJsonPathString(jsonPathAsTextField.getText());
		configBean.setJsonTreeValue(jsonPathInputDialog_jsonPathEditorPane_value);
		configBean.setJsonTreeVisible(jsonPathInputDialog_jsonPathEditorPane_treeVisible);
		
		return configBean;
	}

}
