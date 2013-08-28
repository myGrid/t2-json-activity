/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

import uk.ac.soton.mib104.t2.activities.json.JSONPathActivity;
import uk.ac.soton.mib104.t2.activities.json.ui.config.JSONPathConfigureAction;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceTemplate;

public final class JSONPathConfigureMenuAction extends AbstractConfigureActivityMenuAction<JSONPathActivity> {

	public JSONPathConfigureMenuAction() {
		super(JSONPathActivity.class);
	}

	@Override
	protected Action createAction() {
		final JSONPathActivity activity = this.findActivity();
		
		final Action result = new JSONPathConfigureAction(activity, this.getParentFrame());
		
		result.putValue(Action.NAME, String.format("Configure %s service", JSONPathServiceTemplate.getServiceTemplateName()));
		
		this.addMenuDots(result);
		
		return result;
	}

}
