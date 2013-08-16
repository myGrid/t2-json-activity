package uk.ac.soton.mib104.t2.activities.json;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;

/**
 * Definition bean that describes an input port for an instance of the {@link JSONTemplateActivity} class. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 * @see net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort
 */
public final class JSONTemplateActivityInputPortConfigurationBean implements Serializable {
	
	private static final long serialVersionUID = 842731614446226450L;
	
	/**
	 * Constructs a new instance of this class. 
	 * 
	 * @return  A new instance of this class. 
	 */
	public static final JSONTemplateActivityInputPortConfigurationBean defaultInstance() {
		final JSONTemplateActivityInputPortConfigurationBean bean = new JSONTemplateActivityInputPortConfigurationBean();
		
		bean.setDepth(0);
		bean.setType(defaultType());
		
		return bean;
	}
	
	/**
	 * Returns the default JSON activity port type. 
	 * 
	 * @return  The default JSON activity port type. 
	 */
	public static final JSONActivityPortType defaultType() {
		return JSONActivityPortType.TEXT;
	}

	private int depth;

	private JSONActivityPortType type;
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (obj instanceof JSONTemplateActivityInputPortConfigurationBean) {
			final JSONTemplateActivityInputPortConfigurationBean other = (JSONTemplateActivityInputPortConfigurationBean) obj;
			
			return ObjectUtils.equals(this.getDepth(), other.getDepth()) && ObjectUtils.equals(this.getType(), other.getType());
		} else {
			return false;
		}
	}

	/**
	 * The depth of this input port.
	 * 
	 * @return  The depth of this input port.
	 * @see net.sf.taverna.t2.workflowmodel.Port#getDepth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * The type of this input port.
	 * 
	 * @return  The type of this input port.
	 */
	public JSONActivityPortType getType() {
		return type;
	}

	/**
	 * Set the depth of this input port. 
	 * 
	 * @param depth  The new depth of this input port. 
	 */
	public void setDepth(final int depth) {
		this.depth = depth;
	}

	/**
	 * Set the type of this input port. 
	 * 
	 * @param type  The new type of this input port. 
	 */
	public void setType(final JSONActivityPortType type) {
		this.type = type;
	}
	
}
