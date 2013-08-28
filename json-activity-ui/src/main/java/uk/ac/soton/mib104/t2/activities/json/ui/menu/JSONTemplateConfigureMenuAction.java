/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
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
