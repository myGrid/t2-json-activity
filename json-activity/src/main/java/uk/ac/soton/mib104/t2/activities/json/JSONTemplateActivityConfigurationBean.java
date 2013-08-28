/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.taverna.t2.workflowmodel.processor.activity.config.ActivityInputPortDefinitionBean;
import net.sf.taverna.t2.workflowmodel.processor.activity.config.ActivityOutputPortDefinitionBean;
import net.sf.taverna.t2.workflowmodel.processor.activity.config.ActivityPortsDefinitionBean;

import org.apache.commons.lang.ObjectUtils;

/**
 * Configuration bean for instances of {@link JSONTemplateActivity} class. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONTemplateActivityConfigurationBean implements Serializable {
	
	private static final long serialVersionUID = 1046812874835284706L;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @return A new instance of this class. 
	 */
	public static final JSONTemplateActivityConfigurationBean defaultInstance() {
		final JSONTemplateActivityConfigurationBean configBean = new JSONTemplateActivityConfigurationBean();
		
		configBean.setInputs(Collections.<String, JSONTemplateActivityInputPortConfigurationBean>emptyMap());
		configBean.setOutputs(Collections.<String, String>emptyMap());
		
		return configBean;
	}

	private Map<String, JSONTemplateActivityInputPortConfigurationBean> inputPorts;
	
	private Map<String, String> outputPorts;

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (obj instanceof JSONTemplateActivityConfigurationBean) {
			final JSONTemplateActivityConfigurationBean other = (JSONTemplateActivityConfigurationBean) obj;
			
			return ObjectUtils.equals(this.getInputPorts(), other.getInputPorts()) && ObjectUtils.equals(this.getOutputPorts(), other.getOutputPorts());
		} else {
			return false;
		}
	}

	/**
	 * Returns the map of named input ports. 
	 * 
	 * @return  The map of named input ports. 
	 */
	public Map<String, JSONTemplateActivityInputPortConfigurationBean> getInputPorts() {
		return inputPorts;
	}
	
	/**
	 * Returns the map of named output ports.
	 * 
	 * @return  The map of named output ports.
	 */
	public Map<String, String> getOutputPorts() {
		return outputPorts;
	}

	/**
	 * Set the map of named input ports.
	 * 
	 * @param inputPorts  The new map of named input ports. 
	 */
	public void setInputs(final Map<String, JSONTemplateActivityInputPortConfigurationBean> inputPorts) {
		this.inputPorts = inputPorts;
	}

	/**
	 * Set the map of named output ports.
	 * 
	 * @param outputPorts  The new map of named output ports. 
	 */
	public void setOutputs(final Map<String, String> outputPorts) {
		this.outputPorts = outputPorts;
	}
	
	/**
	 * Returns this configuration bean as an activity ports definition bean. 
	 * 
	 * @return  This configuration bean as an activity ports definition bean. 
	 */
	public ActivityPortsDefinitionBean toPortsDefinition() {
		final List<ActivityInputPortDefinitionBean> inputPortDefinitionBeans = new LinkedList<ActivityInputPortDefinitionBean>();
		final List<ActivityOutputPortDefinitionBean> outputPortDefinitionBeans = new LinkedList<ActivityOutputPortDefinitionBean>();
		
		final Map<String, JSONTemplateActivityInputPortConfigurationBean> inputPortConfigBeans = this.getInputPorts();
		
		if (inputPortConfigBeans != null) {
			for (final Map.Entry<String, JSONTemplateActivityInputPortConfigurationBean> entry : inputPortConfigBeans.entrySet()) {
				final String portName = entry.getKey();
				
				final JSONTemplateActivityInputPortConfigurationBean inputPortConfigBean = entry.getValue();
				
				if (inputPortConfigBean != null) {
					final ActivityInputPortDefinitionBean inputPortDefinitionBean = new ActivityInputPortDefinitionBean();
					
					inputPortDefinitionBean.setAllowsLiteralValues(true);
					inputPortDefinitionBean.setDepth(inputPortConfigBean.getDepth());
					inputPortDefinitionBean.setHandledReferenceSchemes(null);
					inputPortDefinitionBean.setMimeTypes(inputPortConfigBean.getType().getMimeTypes());
					inputPortDefinitionBean.setName(portName);
					inputPortDefinitionBean.setTranslatedElementType(String.class);
					
					inputPortDefinitionBeans.add(inputPortDefinitionBean);
				}
			}
		}
		
		final Map<String, String> outputPortConfigBeans = this.getOutputPorts();
		
		if (outputPortConfigBeans != null) {
			for (final String portName : outputPortConfigBeans.keySet()) {
				final ActivityOutputPortDefinitionBean outputPortDefinitionBean = new ActivityOutputPortDefinitionBean();
				
				outputPortDefinitionBean.setDepth(0);
				outputPortDefinitionBean.setGranularDepth(0);
				outputPortDefinitionBean.setMimeTypes(JSONActivityPortType.JSON.getMimeTypes());
				outputPortDefinitionBean.setName(portName);
				
				outputPortDefinitionBeans.add(outputPortDefinitionBean);
			}
		}
		
		final ActivityPortsDefinitionBean bean = new ActivityPortsDefinitionBean();
		
		bean.setInputPortDefinitions(Collections.unmodifiableList(inputPortDefinitionBeans));
		bean.setOutputPortDefinitions(Collections.unmodifiableList(outputPortDefinitionBeans));
		
		return bean;
	}

}
