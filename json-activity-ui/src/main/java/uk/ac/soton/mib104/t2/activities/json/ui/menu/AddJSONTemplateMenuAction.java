/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URI;

import javax.swing.Action;
import javax.swing.KeyStroke;

import net.sf.taverna.t2.ui.menu.AbstractMenuAction;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;
import net.sf.taverna.t2.workbench.views.graph.actions.DesignOnlyAction;
import net.sf.taverna.t2.workbench.views.graph.menu.InsertMenu;

import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONTemplateServiceIcon;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONTemplateServiceTemplate;

public final class AddJSONTemplateMenuAction extends AbstractMenuAction {

	private static final URI ADD_JSON_TEMPLATE_URI = URI.create("http://taverna.sf.net/2008/t2workbench/menu#graphMenuAddJSONTemplate");

	public AddJSONTemplateMenuAction() {
		super(InsertMenu.INSERT, 350, ADD_JSON_TEMPLATE_URI);
	}

	@Override
	protected Action createAction() {
		return new DesignOnlyAction() {
			
			private static final long serialVersionUID = -7154245232022086114L;

			{
				this.putValue(NAME, JSONTemplateServiceTemplate.getServiceTemplateName());
				this.putValue(SHORT_DESCRIPTION, JSONTemplateServiceTemplate.getServiceTemplateDescription());
				this.putValue(SMALL_ICON, JSONTemplateServiceIcon.getIcon());
				
				this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.SHIFT_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
			}

			@Override
			public void actionPerformed(final ActionEvent e) {
				WorkflowView.importServiceDescription(JSONTemplateServiceTemplate.getServiceDescription(), false);
			}
			
		};
	}
	
}
