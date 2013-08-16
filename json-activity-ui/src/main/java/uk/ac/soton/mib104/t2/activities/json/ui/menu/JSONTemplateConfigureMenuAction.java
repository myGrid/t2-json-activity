package uk.ac.soton.mib104.t2.activities.json.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivity;
import uk.ac.soton.mib104.t2.activities.json.ui.config.JSONTemplateConfigureAction;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONTemplateServiceTemplate;

public final class JSONTemplateConfigureMenuAction extends AbstractConfigureActivityMenuAction<JSONTemplateActivity> {

	public JSONTemplateConfigureMenuAction() {
		super(JSONTemplateActivity.class);
	}

	@Override
	protected Action createAction() {
		final JSONTemplateActivity activity = this.findActivity();
		
		final Action result = new JSONTemplateConfigureAction(activity, this.getParentFrame());
		
		result.putValue(Action.NAME, String.format("Configure %s service", JSONTemplateServiceTemplate.getServiceTemplateName()));
		
		this.addMenuDots(result);
		
		return result;
	}

}
