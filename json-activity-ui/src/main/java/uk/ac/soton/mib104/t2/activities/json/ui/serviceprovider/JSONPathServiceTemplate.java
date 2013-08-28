/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider;

import java.awt.Color;
import java.net.URI;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.AbstractTemplateService;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workbench.ui.impl.configuration.colour.ColourManager;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import uk.ac.soton.mib104.t2.activities.json.JSONPathActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONPathActivityConfigurationBean;

public final class JSONPathServiceTemplate extends AbstractTemplateService<JSONPathActivityConfigurationBean> {

	private static final String description = "A service that evaluates JSONPath (XPath for JSON) expressions";
	
	private static final String name = "JSONPath";

	private static final URI providerId = URI.create("http://www.taverna.org.uk/2013/services/jsonpath");

	static {
		final ColourManager colourManager = ColourManager.getInstance();
		
		if (colourManager != null) {
			colourManager.setPreferredColour(JSONPathActivity.class.getCanonicalName(), Color.decode("#33CC66"));
		}
	}

	public static final ServiceDescription<JSONPathActivityConfigurationBean> getServiceDescription() {
		return (new JSONPathServiceTemplate()).templateService;
	}

	public static final String getServiceTemplateDescription() {
		return description;
	}
	
	public static final String getServiceTemplateName() {
		return name;
	}
	
	@Override
	public Class<? extends Activity<JSONPathActivityConfigurationBean>> getActivityClass() {
		return JSONPathActivity.class;
	}
	
	@Override
	public JSONPathActivityConfigurationBean getActivityConfiguration() {
		return JSONPathActivityConfigurationBean.defaultInstance();
	}
	
	@Override
	public String getDescription() {
		return getServiceTemplateDescription();
	}

	@Override
	public Icon getIcon() {
		return JSONPathServiceIcon.getIcon();
	}

	@Override
	public String getId() {
		return providerId.toASCIIString();
	}
	
	@Override
	public String getName() {
		return getServiceTemplateName();
	}

}
