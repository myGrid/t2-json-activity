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

import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceIcon;
import uk.ac.soton.mib104.t2.activities.json.ui.serviceprovider.JSONPathServiceTemplate;

public final class AddJSONPathMenuAction extends AbstractMenuAction {

	private static final URI ADD_JSON_PATH_URI = URI.create("http://taverna.sf.net/2008/t2workbench/menu#graphMenuAddJSONTemplate");

	public AddJSONPathMenuAction() {
		super(InsertMenu.INSERT, 351, ADD_JSON_PATH_URI);
	}

	@Override
	protected Action createAction() {
		return new DesignOnlyAction() {

			private static final long serialVersionUID = -8149416738822776776L;

			{
				this.putValue(NAME, JSONPathServiceTemplate.getServiceTemplateName());
				this.putValue(SHORT_DESCRIPTION, JSONPathServiceTemplate.getServiceTemplateDescription());
				this.putValue(SMALL_ICON, JSONPathServiceIcon.getIcon());
				
				this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
			}

			@Override
			public void actionPerformed(final ActionEvent e) {
				WorkflowView.importServiceDescription(JSONPathServiceTemplate.getServiceDescription(), false);
			}
			
		};
	}
	
}
