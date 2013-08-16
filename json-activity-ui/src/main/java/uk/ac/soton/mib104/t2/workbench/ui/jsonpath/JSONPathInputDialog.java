package uk.ac.soton.mib104.t2.workbench.ui.jsonpath;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class JSONPathInputDialog extends JDialog {
	
	private static final int defaultOption = JOptionPane.NO_OPTION;
	
	private static final long serialVersionUID = -4116840184162612874L;

	private final JSONPathInputPanel jsonPathInputPane = new JSONPathInputPanel();

	private int option = defaultOption;
	
	public JSONPathInputDialog() {
		this((Window) null);
	}
	
	public JSONPathInputDialog(final Dialog owner) {
		super(owner);
		
		this.initGui();
	}

	public JSONPathInputDialog(final Frame owner) {
		super(owner);
		
		this.initGui();
	}

	public JSONPathInputDialog(final Window owner) {
		super(owner);
		
		this.initGui();
	}

	public final JSONPathInputPanel getJSONPathInputPane() {
		return jsonPathInputPane;
	}

	public final int getOption() {
		return option;
	}

	private void initGui() {
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(final ComponentEvent e) {
				return;
			}

			@Override
			public void componentMoved(final ComponentEvent e) {
				return;
			}

			@Override
			public void componentResized(final ComponentEvent e) {
				return;
			}

			@Override
			public void componentShown(final ComponentEvent e) {
				option = defaultOption;
			}
			
		});
		this.setModal(true);
		this.setResizable(true);
		this.setTitle("Select JSONPath");
		this.setVisible(false);
		
		final JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				option = JOptionPane.OK_OPTION;
				
				JSONPathInputDialog.this.setVisible(false);
			}
			
		});
		
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				option = JOptionPane.CANCEL_OPTION;
				
				JSONPathInputDialog.this.setVisible(false);
			}
			
		});
		
		jsonPathInputPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(okButton);
		buttonGroup.add(cancelButton);
		
		final JPanel buttonsPane = new JPanel(new FlowLayout());
		buttonsPane.add(okButton);
		buttonsPane.add(cancelButton);
		
		final JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(jsonPathInputPane, BorderLayout.CENTER);
		contentPane.add(buttonsPane, BorderLayout.SOUTH);
		this.setContentPane(contentPane);
		
		this.pack();
		
		this.setMinimumSize(this.getSize());
		this.setPreferredSize(this.getSize());
		
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		if (screenSize != null) {
			this.setLocation(Double.valueOf(Math.round((screenSize.getWidth() - this.getWidth()) / 2d)).intValue(), Double.valueOf(Math.round((screenSize.getHeight() - this.getHeight()) / 2d)).intValue());
		}
	}

}
