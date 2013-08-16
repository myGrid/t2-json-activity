package uk.ac.soton.mib104.t2.activities.json;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sf.taverna.t2.workflowmodel.processor.activity.config.ActivityInputPortDefinitionBean;
import net.sf.taverna.t2.workflowmodel.processor.activity.config.ActivityOutputPortDefinitionBean;
import net.sf.taverna.t2.workflowmodel.processor.activity.config.ActivityPortsDefinitionBean;

import org.apache.commons.lang.ObjectUtils;

/**
 * Configuration bean for instances of {@link JSONPathActivity} class.
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONPathActivityConfigurationBean implements Serializable {
	
	/**
	 * Default input port depth.
	 */
	public static final int depthInput = 0;
	
	/**
	 * Default output port depth.
	 */
	public static final int depthOutput = 0;
	
	/**
	 * Default input port name.
	 */
	public static final String portNameInput = "in";
	
	/**
	 * Default output port name (for single values.)
	 */
	public static final String portNameOutput = "out";
	
	/**
	 * Default output port name (for lists.)
	 */
	public static final String portNameOutputList = "outAsList";
	
	private static final long serialVersionUID = 739331325765041151L;

	/**
	 * Default translated element type for input port. 
	 */
	public static final Class<?> translatedElementTypeInput = String.class;

	/**
	 * Constructs a new instance of this class. 
	 * 
	 * @return  A new instance of this class.
	 */
	public static final JSONPathActivityConfigurationBean defaultInstance() {
		final JSONPathActivityConfigurationBean configBean = new JSONPathActivityConfigurationBean();
		
		configBean.setDepth(0);
		configBean.setJsonString("");
		configBean.setJsonPathString("");
		configBean.setJsonTreeValue(null);
		configBean.setJsonTreeVisible(false);
		
		return configBean;
	}
	
	private int depth;
	
	private String jsonPathString;
	
	private String jsonString;
	
	private Object jsonTreeValue;
	
	private boolean jsonTreeVisible;

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (obj instanceof JSONPathActivityConfigurationBean) {
			final JSONPathActivityConfigurationBean other = (JSONPathActivityConfigurationBean) obj;
			
			return ObjectUtils.equals(this.getDepth(), other.getDepth()) && ObjectUtils.equals(this.getJsonString(), other.getJsonString()) && ObjectUtils.equals(this.getJsonPathString(), other.getJsonPathString()) && ObjectUtils.equals(this.getJsonTreeValue(), other.getJsonTreeValue()) && ObjectUtils.equals(this.isJsonTreeVisible(), other.isJsonTreeVisible());
		} else {
			return false;
		}
	}

	public int getDepth() {
		return depth;
	}

	public String getJsonPathString() {
		return jsonPathString;
	}

	public String getJsonString() {
		return jsonString;
	}

	public Object getJsonTreeValue() {
		return jsonTreeValue;
	}

	public boolean isJsonTreeVisible() {
		return jsonTreeVisible;
	}

	public void setDepth(final int depth) {
		this.depth = depth;
	}

	public void setJsonPathString(final String jsonPathString) {
		this.jsonPathString = jsonPathString;
	}

	public void setJsonString(final String jsonString) {
		this.jsonString = jsonString;
	}

	public void setJsonTreeValue(final Object jsonTreeValue) {
		this.jsonTreeValue = jsonTreeValue;
	}

	public void setJsonTreeVisible(final boolean jsonTreeVisible) {
		this.jsonTreeVisible = jsonTreeVisible;
	}
	
	/**
	 * Returns this configuration bean as an activity ports definition bean. 
	 * 
	 * @return  This configuration bean as an activity ports definition bean. 
	 */
	public ActivityPortsDefinitionBean toPortsDefinition() {
		final List<ActivityInputPortDefinitionBean> inputPortDefinitionBeans = new LinkedList<ActivityInputPortDefinitionBean>();
		final List<ActivityOutputPortDefinitionBean> outputPortDefinitionBeans = new LinkedList<ActivityOutputPortDefinitionBean>();
		
		{
			final ActivityInputPortDefinitionBean inputPortDefinitionBean = new ActivityInputPortDefinitionBean();
			
			inputPortDefinitionBean.setAllowsLiteralValues(true);
			inputPortDefinitionBean.setDepth(depthInput);
			inputPortDefinitionBean.setHandledReferenceSchemes(null);
			inputPortDefinitionBean.setMimeTypes(JSONActivityPortType.JSON.getMimeTypes());
			inputPortDefinitionBean.setName(portNameInput);
			inputPortDefinitionBean.setTranslatedElementType(translatedElementTypeInput);
			
			inputPortDefinitionBeans.add(inputPortDefinitionBean);
		}
		
		{
			final ActivityOutputPortDefinitionBean outputPortDefinitionBean = new ActivityOutputPortDefinitionBean();
			
			outputPortDefinitionBean.setDepth(depthOutput);
			outputPortDefinitionBean.setGranularDepth(depthOutput);
			outputPortDefinitionBean.setMimeTypes(JSONActivityPortType.JSON.getMimeTypes());
			outputPortDefinitionBean.setName(portNameOutput);
			
			outputPortDefinitionBeans.add(outputPortDefinitionBean);
		}
		
		final int depth = this.getDepth();
		
		if (depth > 0) {
			final ActivityOutputPortDefinitionBean outputPortDefinitionBean = new ActivityOutputPortDefinitionBean();
			
			outputPortDefinitionBean.setDepth(depth);
			outputPortDefinitionBean.setGranularDepth(depth);
			outputPortDefinitionBean.setMimeTypes(JSONActivityPortType.JSON.getMimeTypes());
			outputPortDefinitionBean.setName(portNameOutputList);
			
			outputPortDefinitionBeans.add(outputPortDefinitionBean);
		}
		
		final ActivityPortsDefinitionBean bean = new ActivityPortsDefinitionBean();
		
		bean.setInputPortDefinitions(Collections.unmodifiableList(inputPortDefinitionBeans));
		bean.setOutputPortDefinitions(Collections.unmodifiableList(outputPortDefinitionBeans));
		
		return bean;
	}
	
}
