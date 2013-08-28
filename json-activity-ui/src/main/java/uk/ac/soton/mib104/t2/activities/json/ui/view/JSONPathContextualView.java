/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.view;

import java.awt.Frame;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;

import uk.ac.soton.mib104.t2.activities.json.JSONPathActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONPathActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.ui.config.JSONPathConfigureAction;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceTemplate;

public final class JSONPathContextualView extends HTMLBasedActivityContextualView<JSONPathActivityConfigurationBean> {
	
	private static final long serialVersionUID = 2509212838953166336L;

	public JSONPathContextualView(final JSONPathActivity activity) {
		super(activity);
	}

	@Override
	public String getBackgroundColour() {
		return "#33CC66";
	}

	@Override
	public Action getConfigureAction(final Frame owner) {
		return new JSONPathConfigureAction((JSONPathActivity) getActivity(), owner);
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}

	@Override
	protected String getRawTableRowsHtml() {
		final JSONPathActivityConfigurationBean configBean = this.getConfigBean();
		
		final StringBuffer sb = new StringBuffer();
		
		sb.append("<tr><th>Input Port Name</th><th>Depth</th></tr>");
		
		sb.append(String.format("<tr><td>%s</td><td>%d</td></tr>", JSONPathActivityConfigurationBean.portNameInput, JSONPathActivityConfigurationBean.depthInput));
		
		sb.append("<tr><th>Output Port Name</th><th>Depth</th></tr>");
		
		sb.append(String.format("<tr><td>%s</td><td>%d</td></tr>", JSONPathActivityConfigurationBean.portNameOutput, JSONPathActivityConfigurationBean.depthOutput));
		
		final int depth = configBean.getDepth();
		
		if (depth > 0) {
			sb.append(String.format("<tr><td>%s</td><td>%d</td></tr>", JSONPathActivityConfigurationBean.portNameOutputList, depth));
		}
		
		return sb.toString();
	}

	@Override
	public String getViewTitle() {
		return String.format("%s service", JSONPathServiceTemplate.getServiceTemplateName());
	}

}
