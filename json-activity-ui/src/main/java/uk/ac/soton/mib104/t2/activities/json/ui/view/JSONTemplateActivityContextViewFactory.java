package uk.ac.soton.mib104.t2.activities.json.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivity;

public final class JSONTemplateActivityContextViewFactory implements ContextualViewFactory<JSONTemplateActivity> {

	@Override
	public boolean canHandle(final Object selection) {
		if (selection == null) {
			return false;
		} else {
			return (selection instanceof JSONTemplateActivity);
		}
	}

	@Override
	public List<ContextualView> getViews(final JSONTemplateActivity selection) {
		return Arrays.<ContextualView>asList(new JSONTemplateContextualView(selection));
	}
	
}
