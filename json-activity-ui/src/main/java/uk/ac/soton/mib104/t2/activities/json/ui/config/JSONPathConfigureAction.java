/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import uk.ac.soton.mib104.t2.activities.json.JSONPathActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONPathActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceIcon;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceTemplate;

public final class JSONPathConfigureAction extends ActivityConfigurationAction<JSONPathActivity, JSONPathActivityConfigurationBean> {

	private static final long serialVersionUID = 2220146812120006850L;

	public JSONPathConfigureAction(final JSONPathActivity activity, final Frame owner) {
		super(activity);
		
		this.putValue(NAME, String.format("Configure %s service", JSONPathServiceTemplate.getServiceTemplateName()));
		this.putValue(SMALL_ICON, JSONPathServiceIcon.getIcon());
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final JSONPathActivity activity = this.getActivity();
		
		@SuppressWarnings("unchecked")
		final ActivityConfigurationDialog<JSONPathActivity, JSONPathActivityConfigurationBean> currentDialog = ActivityConfigurationAction.getDialog(activity);
		
		if (currentDialog == null) {
			ActivityConfigurationAction.setDialog(activity, new ActivityConfigurationDialog<JSONPathActivity, JSONPathActivityConfigurationBean>(activity, new JSONPathConfigurationPanel(activity)));
		} else {
			currentDialog.toFront();
		}

	}

}
