/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json.ui.config;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.famfamfam.Silk;

import uk.ac.soton.mib104.t2.activities.json.JSONActivityPortType;
import uk.ac.soton.mib104.t2.workbench.ui.PortDepthInputPanel;

public final class JSONTemplateInputPortConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 8707940408277936750L;
	
	private final PortDepthInputPanel portDepthInputPane;
	
	private final JComboBox portTypeComboBox;

	public JSONTemplateInputPortConfigurationPanel() {
		super(new GridBagLayout());
		
		portDepthInputPane = new PortDepthInputPanel();
		portTypeComboBox = new JComboBox(JSONActivityPortType.values());
		
		final JLabel portDepthLabel = new JLabel("Type:");
		portDepthLabel.setFont(portDepthLabel.getFont().deriveFont(11f));
		portDepthLabel.setHorizontalAlignment(JLabel.LEFT);
		portDepthLabel.setIcon(Silk.getHelpIcon());
		portDepthLabel.setToolTipText("The granularity of this input port: single value (0), list of values (1), list of lists (2), etc.");

		final JLabel portTypeLabel = new JLabel("Value type:");
		portTypeLabel.setFont(portTypeLabel.getFont().deriveFont(11f));
		portTypeLabel.setHorizontalAlignment(JLabel.LEFT);
		portTypeLabel.setIcon(Silk.getHelpIcon());
		portTypeLabel.setToolTipText("The data type for values of this input port.");
		
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.ipadx = 8;
		this.add(portDepthLabel, constraints);
		constraints.ipadx = 0;
		constraints.gridx++;
		this.add(portDepthInputPane, constraints);
		constraints.gridx = 0; constraints.gridy++; // carriage return
		constraints.gridwidth = 2;
		this.add(new JSeparator(JSeparator.HORIZONTAL), constraints);
		constraints.gridwidth = 1;
		constraints.gridx = 0; constraints.gridy++; // carriage return
		constraints.ipadx = 8;
		this.add(portTypeLabel, constraints);
		constraints.ipadx = 0;
		constraints.gridx++;
		this.add(portTypeComboBox, constraints);
	}

	public int getDepth() {
		return portDepthInputPane.getPortDepth();
	}
	
	public JSONActivityPortType getType() {
		return (JSONActivityPortType) portTypeComboBox.getSelectedItem();
	}

	public void setDepth(final int depth) {
		portDepthInputPane.setPortDepth(depth);
	}
	
	public void setType(final JSONActivityPortType type) {
		portTypeComboBox.setSelectedItem(type);
	}
	
}
