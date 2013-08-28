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

import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONTemplateServiceTemplate;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONTemplateServiceIcon;

public final class JSONTemplateConfigureAction extends ActivityConfigurationAction<JSONTemplateActivity, JSONTemplateActivityConfigurationBean> {

	private static final long serialVersionUID = 4676474619164379333L;

	public JSONTemplateConfigureAction(final JSONTemplateActivity activity, final Frame owner) {
		super(activity);
		
		this.putValue(NAME, String.format("Configure %s service", JSONTemplateServiceTemplate.getServiceTemplateName()));
		this.putValue(SMALL_ICON, JSONTemplateServiceIcon.getIcon());
	}

	public void actionPerformed(final ActionEvent e) {
		final JSONTemplateActivity activity = this.getActivity();
		
		@SuppressWarnings("unchecked")
		final ActivityConfigurationDialog<JSONTemplateActivity, JSONTemplateActivityConfigurationBean> currentDialog = ActivityConfigurationAction.getDialog(activity);
		
		if (currentDialog == null) {
			ActivityConfigurationAction.setDialog(activity, new ActivityConfigurationDialog<JSONTemplateActivity, JSONTemplateActivityConfigurationBean>(activity, new JSONTemplateConfigurationPanel(activity)));
		} else {
			currentDialog.toFront();
		}
	}

}
