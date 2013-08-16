package uk.ac.soton.mib104.t2.activities.json.ui.view;

import java.awt.Frame;
import java.util.Map;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;

import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityInputPortConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.ui.config.JSONTemplateConfigureAction;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONTemplateServiceTemplate;

public final class JSONTemplateContextualView extends HTMLBasedActivityContextualView<JSONTemplateActivityConfigurationBean> {
	
	private static final long serialVersionUID = 1651405460448158336L;

	public JSONTemplateContextualView(final JSONTemplateActivity activity) {
		super(activity);
	}

	@Override
	public String getBackgroundColour() {
		return "#FFCC66";
	}

	@Override
	public Action getConfigureAction(final Frame owner) {
		return new JSONTemplateConfigureAction((JSONTemplateActivity) getActivity(), owner);
	}

	@Override
	public int getPreferredPosition() {
		return 100;
	}

	@Override
	protected String getRawTableRowsHtml() {
		final JSONTemplateActivityConfigurationBean configBean = this.getConfigBean();
		
		final StringBuffer sb = new StringBuffer();
		
		sb.append("<tr>");
		sb.append("<th>Input Port Name (Value Type)</th>");
		sb.append("<th>Depth</th>");
		sb.append("</tr>");
		
		for (final Map.Entry<String, JSONTemplateActivityInputPortConfigurationBean> entry : configBean.getInputPorts().entrySet()) {
			final String portName = entry.getKey();
			
			final JSONTemplateActivityInputPortConfigurationBean inputPortBean = entry.getValue();
			
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(portName);
			sb.append(" (");
			sb.append(inputPortBean.getType());
			sb.append(')');
			sb.append("</td>");
			sb.append("<td>");
			sb.append(inputPortBean.getDepth());
			sb.append("</td>");
			sb.append("</tr>");
		}
		
		sb.append("<tr>");
		sb.append("<th>Output Port Name</th>");
		sb.append("<th>Depth</th>");
		sb.append("</tr>");
		
		for (final String portName : configBean.getOutputPorts().keySet()) {
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(portName);
			sb.append("</td>");
			sb.append("<td>");
			sb.append(0);
			sb.append("</td>");
			sb.append("</tr>");
		}
		
		return sb.toString();
	}

	@Override
	public String getViewTitle() {
		return String.format("%s service", JSONTemplateServiceTemplate.getServiceTemplateName());
	}

}
