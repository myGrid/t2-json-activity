/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.config;

import java.awt.Dimension;
import java.util.Map;
import java.util.Set;

import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityInputPortConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.VelocityTemplateUtils;
import uk.ac.soton.mib104.t2.workbench.ui.ActivityConfigurationPanelWithInputPortAndOutputPortComponents;
import uk.ac.soton.mib104.t2.workbench.ui.StringUtils;
import uk.ac.soton.mib104.t2.workbench.ui.json.text.JSONTemplateKeywordDocumentEditorPane;

public final class JSONTemplateConfigurationPanel extends ActivityConfigurationPanelWithInputPortAndOutputPortComponents<JSONTemplateActivity, JSONTemplateActivityConfigurationBean, JSONTemplateActivityInputPortConfigurationBean, String, JSONTemplateInputPortConfigurationPanel, JSONTemplateKeywordDocumentEditorPane> {
	
	private static final Dimension defaultSize = new Dimension(540, 405);
	
	private static final String lineSeparator = System.getProperty("line.separator");

	private static final long serialVersionUID = -3706062783819452419L;
	
	private static final String createTemplate(final Set<String> portNames) {
		if (portNames == null) {
			throw new IllegalArgumentException(new NullPointerException("portNames"));
		}
		
		final StringBuffer sb = new StringBuffer();
		
		final int portNamesCount = (portNames == null) ? 0 : portNames.size();
		
		sb.append(String.format("## Auto-generated template for %s", StringUtils.pluralize(portNamesCount, "%d input port", "%d input ports")));
		
		if (portNamesCount > 0) {
			sb.append(':');
			sb.append(lineSeparator);
			sb.append("##   ");
			
			if (portNames != null) {
				final int minLength = sb.length();
				
				for (final String portName : portNames) {
					if (sb.length() > minLength) {
						sb.append(", ");
					}
					
					sb.append(portName);
				}
			}
		}
		
		sb.append(lineSeparator);
		sb.append(lineSeparator);
		
		sb.append(String.format("{%s  \"inputs\": {", lineSeparator));
		
		if (portNamesCount > 0) {
			final int minLength = sb.length();
			
			for (final String portName : portNames) {
				if (sb.length() > minLength) {
					sb.append(',');
				}
				
				sb.append(String.format("%s    \"", lineSeparator));
				sb.append(portName);
				sb.append("\": $");
				sb.append(portName);
			}
		} else {
			sb.append(String.format("%s    ## empty", lineSeparator, portNamesCount));
		}
		
		sb.append(String.format("%s  }%s}", lineSeparator, lineSeparator));
		
		return sb.toString();
	}

	public JSONTemplateConfigurationPanel(final JSONTemplateActivity activity) {
		super(activity);
	}

	private void addAllPorts(final JSONTemplateKeywordDocumentEditorPane outputPortComponent) {
		for (final String portName : this.getInputPortComponents().keySet()) {
			outputPortComponent.addPort(VelocityTemplateUtils.toASTReferenceLiteral(portName, true));
			outputPortComponent.addPort(VelocityTemplateUtils.toASTReferenceLiteral(portName, false));
		}
	}
	
	private void addPort(final String portName) {
		for (final JSONTemplateKeywordDocumentEditorPane outputPortComponent : this.getOutputPortComponents().values()) {					
			outputPortComponent.addPort(VelocityTemplateUtils.toASTReferenceLiteral(portName, true));
			outputPortComponent.addPort(VelocityTemplateUtils.toASTReferenceLiteral(portName, false));
		}
	}

	@Override
	protected boolean checkInputPortComponentValues(final JSONTemplateInputPortConfigurationPanel inputPortComponent) {
		return true;
	}

	@Override
	protected boolean checkOutputPortComponentValues(final JSONTemplateKeywordDocumentEditorPane outputPortComponent) {
		return true;
	}

	public String defaultTemplate() {
		return createTemplate(this.getInputPortComponents().keySet());
	}
	
	@Override
	protected Map<String, JSONTemplateActivityInputPortConfigurationBean> getInputPortConfigurations(final JSONTemplateActivityConfigurationBean configBean) {
		return configBean.getInputPorts();
	}
	
	@Override
	protected Map<String, String> getOutputPortConfigurations(final JSONTemplateActivityConfigurationBean configBean) {
		return configBean.getOutputPorts();
	}
	
	@Override
	protected void initGui() {
		super.initGui();
		
		this.setMinimumSize(defaultSize);
		this.setPreferredSize(this.getMinimumSize());
	}
	
	@Override
	protected void inputPortAdded(final String portName, final JSONTemplateInputPortConfigurationPanel inputPortComponent) {
		super.inputPortAdded(portName, inputPortComponent);
		
		this.addPort(portName);
	}
	
	@Override
	protected void inputPortNameChanged(final String oldPortName, final String newPortName, final JSONTemplateInputPortConfigurationPanel inputPortComponent) {
		super.inputPortNameChanged(oldPortName, newPortName, inputPortComponent);
		
		this.removePort(oldPortName);
		
		this.addPort(newPortName);
	}

	@Override
	protected void inputPortRemoved(final String portName, final JSONTemplateInputPortConfigurationPanel inputPortComponent) {
		super.inputPortRemoved(portName, inputPortComponent);
		
		this.removePort(portName);
	}
	
	@Override
	protected boolean isPortName(final CharSequence input) {
		return VelocityTemplateUtils.isVariableName(input);
	}

	@Override
	protected JSONTemplateActivityConfigurationBean newConfiguration() {
		return JSONTemplateActivityConfigurationBean.defaultInstance();
	}
	
	@Override
	protected JSONTemplateActivityInputPortConfigurationBean newInputPortConfiguration() {
		return JSONTemplateActivityInputPortConfigurationBean.defaultInstance();
	}
	
	@Override
	protected String newOutputPortConfiguration() {
		return this.defaultTemplate();
	}

	@Override
	protected void outputPortAdded(final String portName, final JSONTemplateKeywordDocumentEditorPane outputPortComponent) {
		super.outputPortAdded(portName, outputPortComponent);
		
		this.addAllPorts(outputPortComponent);
	}

	@Override
	protected void outputPortRemoved(final String portName, final JSONTemplateKeywordDocumentEditorPane outputPortComponent) {
		super.outputPortRemoved(portName, outputPortComponent);
		
		this.removeAllPorts(outputPortComponent);
	}
	
	private void removeAllPorts(final JSONTemplateKeywordDocumentEditorPane outputPortComponent) {
		for (final String portName : this.getInputPortComponents().keySet()) {
			outputPortComponent.removePort(VelocityTemplateUtils.toASTReferenceLiteral(portName, true));
			outputPortComponent.removePort(VelocityTemplateUtils.toASTReferenceLiteral(portName, false));
		}
	}
	
	private void removePort(final String portName) {
		for (final JSONTemplateKeywordDocumentEditorPane outputPortComponent : this.getOutputPortComponents().values()) {
			outputPortComponent.removePort(VelocityTemplateUtils.toASTReferenceLiteral(portName, true));
			outputPortComponent.removePort(VelocityTemplateUtils.toASTReferenceLiteral(portName, false));
		}
	}

	@Override
	protected void setInputPortConfigurations(final JSONTemplateActivityConfigurationBean configBean, final Map<String, JSONTemplateActivityInputPortConfigurationBean> inputPortConfigBeans) {
		configBean.setInputs(inputPortConfigBeans);
	}

	@Override
	protected void setOutputPortConfigurations(final JSONTemplateActivityConfigurationBean configBean, final Map<String, String> outputPortConfigBeans) {
		configBean.setOutputs(outputPortConfigBeans);
	}

	@Override
	protected JSONTemplateInputPortConfigurationPanel toInputPortComponent(final String portName, final JSONTemplateActivityInputPortConfigurationBean inputPortConfigBean) {
		final JSONTemplateInputPortConfigurationPanel inputPortComponent = new JSONTemplateInputPortConfigurationPanel();
		
		inputPortComponent.setDepth(inputPortConfigBean.getDepth());
		inputPortComponent.setType(inputPortConfigBean.getType());
		
		return inputPortComponent;
	}

	@Override
	protected JSONTemplateActivityInputPortConfigurationBean toInputPortConfiguration(final String portName, final JSONTemplateInputPortConfigurationPanel inputPortComponent) {
		final JSONTemplateActivityInputPortConfigurationBean inputPortConfigBean = new JSONTemplateActivityInputPortConfigurationBean();
		
		inputPortConfigBean.setDepth(inputPortComponent.getDepth());
		inputPortConfigBean.setType(inputPortComponent.getType());
		
		return inputPortConfigBean;
	}
	
	@Override
	protected JSONTemplateKeywordDocumentEditorPane toOutputPortComponent(final String portName, final String outputPortConfigBean) {
		return new JSONTemplateKeywordDocumentEditorPane(this, outputPortConfigBean);
	}

	@Override
	protected String toOutputPortConfiguration(final String portName, final JSONTemplateKeywordDocumentEditorPane outputPortComponent) {
		return outputPortComponent.getText();
	}
	
}
