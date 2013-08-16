package uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider;

import java.awt.Color;
import java.net.URI;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.AbstractTemplateService;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workbench.ui.impl.configuration.colour.ColourManager;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityConfigurationBean;

public final class JSONTemplateServiceTemplate extends AbstractTemplateService<JSONTemplateActivityConfigurationBean> {
	
	private static final String description = "A service that uses templates to generate JSON data structures";
	
	private static final String name = "JSON Template";
	
	private static final URI providerId = URI.create("http://www.taverna.org.uk/2013/services/json-template");
	
	static {
		final ColourManager colourManager = ColourManager.getInstance();
		
		if (colourManager != null) {
			colourManager.setPreferredColour(JSONTemplateActivity.class.getCanonicalName(), Color.decode("#FFCC66"));
		}
	}
	
	public static final ServiceDescription<JSONTemplateActivityConfigurationBean> getServiceDescription() {
		return (new JSONTemplateServiceTemplate()).templateService;
	}
	
	public static final String getServiceTemplateDescription() {
		return description;
	}
	
	public static final String getServiceTemplateName() {
		return name;
	}
	
	@Override
	public Class<? extends Activity<JSONTemplateActivityConfigurationBean>> getActivityClass() {
		return JSONTemplateActivity.class;
	}

	@Override
	public JSONTemplateActivityConfigurationBean getActivityConfiguration() {
		return JSONTemplateActivityConfigurationBean.defaultInstance();
	}

	@Override
	public String getDescription() {
		return getServiceTemplateDescription();
	}
	
	@Override
	public Icon getIcon() {
		return JSONTemplateServiceIcon.getIcon();
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
