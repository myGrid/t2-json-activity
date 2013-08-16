package uk.ac.soton.mib104.t2.activities.json.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import uk.ac.soton.mib104.t2.activities.json.JSONPathActivity;

public final class JSONPathActivityContextViewFactory implements ContextualViewFactory<JSONPathActivity> {

	@Override
	public boolean canHandle(final Object object) {
		if (object == null) {
			return false;
		} else {
			return (object instanceof JSONPathActivity);
		}
	}

	@Override
	public List<ContextualView> getViews(final JSONPathActivity selection) {
		return Arrays.<ContextualView>asList(new JSONPathContextualView(selection));
	}
	
}
