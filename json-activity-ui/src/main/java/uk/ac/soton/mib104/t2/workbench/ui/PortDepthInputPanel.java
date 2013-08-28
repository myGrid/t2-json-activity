/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.workbench.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Component for configuring the depth of a port (or "port depth"). 
 * 
 * @author Mark Borkum
 * @author David Withers
 * @version 0.0.1-SNAPSHOT
 * 
 * @see net.sf.taverna.t2.workbench.design.ui.DataflowInputPortPanel
 */
public final class PortDepthInputPanel extends JPanel {

	private static final int defaultMaxPortDepth = 100;
	
	private static final int minPortDepth = 0;
	
	private static final long serialVersionUID = -3098046588273033859L;
	
	private final JLabel listDepthLabel;
	
	private final JSpinner listDepthSpinner;
	
	private final JRadioButton listValueButton;
	
	private final int maxPortDepth;
	
	private final JRadioButton singleValueButton;
	
	/**
	 * Empty constructor (uses default maximum port depth.)
	 * <p>
	 * Equivalent to: <code>PortDepthInputPanel(100);</code>
	 */
	public PortDepthInputPanel() {
		this(defaultMaxPortDepth);
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param maxPortDepth  the maximum port depth
	 */
	public PortDepthInputPanel(final int maxPortDepth) {
		super(new GridBagLayout());
		
		if (minPortDepth >= maxPortDepth) {
			throw new IllegalArgumentException(String.format("Invalid maximum port depth (less than %d): %d", minPortDepth, maxPortDepth));
		}
		
		this.maxPortDepth = maxPortDepth;
		
		singleValueButton = new JRadioButton();
		singleValueButton.setText("Single value");
		
		listValueButton = new JRadioButton();
		listValueButton.setText("List of depth");
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(singleValueButton);
		buttonGroup.add(listValueButton);
		
		listDepthSpinner = new JSpinner(new SpinnerNumberModel(minPortDepth + 1, minPortDepth + 1, maxPortDepth, 1));

		listDepthLabel = new JLabel();
		listDepthLabel.setFont(listDepthLabel.getFont().deriveFont(11f));
		listDepthLabel.setText("Depth 1 is a list, 2 is a list of lists, etc.");
		
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 2;
		this.add(singleValueButton, constraints);
		constraints.gridwidth = 1;
		constraints.gridy = 1;
		this.add(listValueButton, constraints);
		constraints.gridx = 1;
		this.add(listDepthSpinner, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.weighty = 1d;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		this.add(listDepthLabel, constraints);
		
		singleValueButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(final ItemEvent e) {
				final boolean listDepthEnabled = (ItemEvent.SELECTED != e.getStateChange());
				
				listDepthSpinner.setEnabled(listDepthEnabled);
				listDepthLabel.setEnabled(listDepthEnabled);
			}
			
		});
		singleValueButton.setSelected(true);
	}
	
	/**
	 * Returns the port depth.
	 *
	 * @return  The port depth.
	 */
	public int getPortDepth() {
		if (singleValueButton.isSelected()) {
			return minPortDepth;
		} else {
			return (Integer) listDepthSpinner.getValue();
		}
	}
	
	/**
	 * Sets the port depth.
	 *
	 * @param depth  The port depth.
	 * @throws IllegalArgumentException  If the given port depth is invalid. 
	 */
	public void setPortDepth(final int depth) {
		if (depth < minPortDepth) {
			throw new IllegalArgumentException(String.format("Invalid port depth (too small): %d < %d", depth, minPortDepth));
		} else if (depth > maxPortDepth) {
			throw new IllegalArgumentException(String.format("Invalid port depth (too big): %d > %d", depth, maxPortDepth));
		} else if (depth > minPortDepth) {
			listValueButton.setSelected(true);
			listDepthSpinner.setValue(depth);
		} else {
			singleValueButton.setSelected(true);
		}
	}
	
}
