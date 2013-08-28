/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.menu;

import java.awt.event.ActionEvent;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sf.taverna.t2.ui.menu.AbstractContextualMenuAction;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;
import net.sf.taverna.t2.workflowmodel.Dataflow;

import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceIcon;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceTemplate;

public final class AddJSONPathAction extends AbstractContextualMenuAction {

	private static final URI INSERT_URI = URI.create("http://taverna.sf.net/2009/contextMenu/insert");

	public AddJSONPathAction() {
		super(INSERT_URI, 351);
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && (this.getContextualSelection().getSelection() instanceof Dataflow);
	}

	@Override
	protected Action createAction() {
		return new AbstractAction() {

			private static final long serialVersionUID = -6252340865703824960L;

			{
				this.putValue(NAME, JSONPathServiceTemplate.getServiceTemplateName());
				this.putValue(SHORT_DESCRIPTION, JSONPathServiceTemplate.getServiceTemplateDescription());
				this.putValue(SMALL_ICON, JSONPathServiceIcon.getIcon());
			}

			@Override
			public void actionPerformed(final ActionEvent e) {
				WorkflowView.importServiceDescription(JSONPathServiceTemplate.getServiceDescription(), false);
			}
			
		};
	}
	
}
