/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.workbench.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import net.sf.taverna.t2.workbench.icons.WorkbenchIcons;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.apache.commons.lang.ObjectUtils;

/**
 * Activity configuration panel (with input port and output port components.)
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 *
 * @param <ACTIVITY>  the type of the activity
 * @param <CONFIG>  the type of the configuration bean for the activity
 * @param <IN_CONFIG>  the type of the configuration bean for input ports
 * @param <OUT_CONFIG>  the type of the configuration bean for output ports
 * @param <IN_COMPONENT>  the type of the component for input ports
 * @param <OUT_COMPONENT>  the type of the component for output ports
 */
public abstract class ActivityConfigurationPanelWithInputPortAndOutputPortComponents<ACTIVITY extends Activity<CONFIG>, CONFIG, IN_CONFIG, OUT_CONFIG, IN_COMPONENT extends Component, OUT_COMPONENT extends Component> extends ActivityConfigurationPanel<ACTIVITY, CONFIG> {
	
	private static abstract class AbstractJTabbedPaneWithTabTitleAdapterAndButtonTabComponent extends JTabbedPane {

		/**
		 * 
		 * @link http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabComponentsDemoProject/src/components/ButtonTabComponent.java
		 */
		private static final class ButtonTabComponent extends JPanel {
			
			private class TabButton extends JButton implements ActionListener {
				
				private static final int delta = 6;
				
				private static final long serialVersionUID = 5702371651363313007L;
				
				private static final int size = 17;

				public TabButton() {
					this.setPreferredSize(new Dimension(size, size));
					
					this.setToolTipText("close this tab");
					
					this.setUI(new BasicButtonUI());
					
					this.setContentAreaFilled(false);
					
					this.setFocusable(false);
					
					this.setBorder(BorderFactory.createEtchedBorder());
					this.setBorderPainted(false);
					
					this.addMouseListener(buttonMouseListener);
					this.setRolloverEnabled(true);
					
					this.addActionListener(this);
				}

				@Override
				public void actionPerformed(final ActionEvent e) {
					final int index = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);
					
					if (index != -1) {
						final String title = tabbedPane.getTitleAt(index);
						
						switch (JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(tabbedPane), String.format("<html><body width=\"380\">You have configured the tab \"%s\". If you close it, your changes will be lost. Do you want to close it anyway?</body></html>", title), "Are you sure you want to close this tab?", JOptionPane.YES_NO_CANCEL_OPTION)) {
						case JOptionPane.YES_OPTION:
							break;
						default:
							return;
						}
						
						tabbedPane.removeTabAt(index);
					}
				}
				
				@Override
				protected void paintComponent(final Graphics g) {
					super.paintComponent(g);
					
					final Graphics2D g2 = (Graphics2D) g.create();
					
					if (this.getModel().isPressed()) {
						g2.translate(1, 1);
					}
					
					g2.setStroke(new BasicStroke(2));
					g2.setColor(Color.BLACK);
					
					if (this.getModel().isRollover()) {
						g2.setColor(Color.RED);
					}
					
					g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
					g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
					
					g2.dispose();
				}

				@Override
				public void updateUI() {
					return;
				}
			}

			private final static MouseListener buttonMouseListener = new MouseAdapter() {
				
				@Override
				public void mouseEntered(final MouseEvent e) {
					final Component component = e.getComponent();
					
					if (component instanceof AbstractButton) {
						final AbstractButton button = (AbstractButton) component;
						
						button.setBorderPainted(true);
					}
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					final Component component = e.getComponent();
					
					if (component instanceof AbstractButton) {
						final AbstractButton button = (AbstractButton) component;
						
						button.setBorderPainted(false);
					}
				}
				
			};

			private static final Border defaultButtonBorder = BorderFactory.createEmptyBorder(2, 0, 0, 0);

			private static final Border defaultLabelBorder = BorderFactory.createEmptyBorder(0, 7, 0, 5);

			private static final long serialVersionUID = -2426393757304130401L;
			
			private final JTabbedPane tabbedPane;

			public ButtonTabComponent(final JTabbedPane tabbedPane) {
				super(new FlowLayout(FlowLayout.LEFT, 0, 0));
				
				if (tabbedPane == null) {
					throw new IllegalArgumentException(new NullPointerException("tabbedPane"));
				}
				
				this.tabbedPane = tabbedPane;
				
				this.setBorder(defaultButtonBorder);
				this.setOpaque(false);

				final JLabel label = new JLabel() {
					
					private static final long serialVersionUID = 7209206022355197846L;

					@Override
					public String getText() {
						final int index = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);
					
						if (index != -1) {
							return tabbedPane.getTitleAt(index);
						}
						
						return null;
					}

					@Override
					public void setIcon(final Icon icon) {
						if (icon != null) {
							throw new UnsupportedOperationException();
						}
					}

				};
				label.setBorder(defaultLabelBorder);
				
				this.add(label);
				this.add(new TabButton());
			}
			
		}
		
		/**
		 * 
		 * @link http://java-swing-tips.googlecode.com/svn/trunk/TabTitleEditor/src/java/example/MainPanel.java
		 */
		private static final class TabTitleAdapter extends MouseAdapter implements ChangeListener {
			
			private Component currentComponent = null;
			
			private Dimension currentDimension = null;
			
			private int currentIndex = -1;
			
			private int currentLength = -1;
			
			private final JTextField editor = new JTextField();
			
			private final AbstractJTabbedPaneWithTabTitleAdapterAndButtonTabComponent tabbedPane;

			public TabTitleAdapter(final AbstractJTabbedPaneWithTabTitleAdapterAndButtonTabComponent tabbedPane) {
				super();
				
				if (tabbedPane == null) {
					throw new IllegalArgumentException(new NullPointerException("tabbedPane"));
				}
				
				this.tabbedPane = tabbedPane;
				
				tabbedPane.addChangeListener(this);
				tabbedPane.addMouseListener(this);
				
				editor.setBorder(BorderFactory.createEmptyBorder());
				editor.addFocusListener(new FocusAdapter() {
					
					@Override
					public void focusLost(final FocusEvent e) {
//						renameTabTitle();
						cancelEditing();
					}
					
				});
				editor.addKeyListener(new KeyAdapter() {
					
					@Override
					public void keyPressed(final KeyEvent e) {
						switch (e.getKeyCode()) {
						case KeyEvent.VK_ENTER:
							renameTabTitle();
							
							break;
						case KeyEvent.VK_ESCAPE:
							cancelEditing();
							
							break;
						default:
							editor.setPreferredSize((editor.getText().length() > currentLength) ? null : currentDimension);
							
							tabbedPane.revalidate();
							tabbedPane.repaint();
						}
					}
					
				});
				tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "start-editing");
				tabbedPane.getActionMap().put("start-editing", new AbstractAction() {
						
					private static final long serialVersionUID = 4657327152920108200L;

					@Override
					public void actionPerformed(final ActionEvent e) {
						startEditing();
					}
					
				});
			}

			private void cancelEditing() {
				if (currentIndex == -1) {
					return;
				}
				
				tabbedPane.setTabComponentAt(currentIndex, currentComponent);
				
				currentIndex = -1;
				currentLength = -1;
				currentComponent = null;
				
				editor.setVisible(false);
				editor.setPreferredSize(null);
				
				tabbedPane.requestFocusInWindow();
			}

			@Override
			public void mouseClicked(final MouseEvent e) {
				final Rectangle r = tabbedPane.getUI().getTabBounds(tabbedPane, tabbedPane.getSelectedIndex());
				
				if ((r != null) && r.contains(e.getPoint()) && e.getClickCount() == 2) {
					startEditing();
				} else {
//					renameTabTitle();
					cancelEditing();
				}
			}

			private void renameTabTitle() {
				final String newTitle = editor.getText().trim();
				
				if (currentIndex >= 0) {
					if (tabbedPane.isTabTitleValid(newTitle)) {
						for (int index = 0, length = tabbedPane.getTabCount(); index < length; index++) {
							if ((index != currentIndex) && tabbedPane.getTitleAt(index).equals(newTitle)) {
								cancelEditing();

								JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(tabbedPane), String.format("Duplicate name: %s", newTitle), "Unable to rename tab", JOptionPane.ERROR_MESSAGE);

								return;
							}
						}
						
						tabbedPane.setTitleAt(currentIndex, newTitle);
					} else {
						cancelEditing();

						JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(tabbedPane), String.format("Invalid name: %s", newTitle), "Unable to rename tab", JOptionPane.ERROR_MESSAGE);

						return;
					}
				}
				
				cancelEditing();
			}

			private void startEditing() {
				currentIndex = tabbedPane.getSelectedIndex();
				currentComponent = tabbedPane.getTabComponentAt(currentIndex);
				tabbedPane.setTabComponentAt(currentIndex, editor);
				editor.setVisible(true);
				editor.setText(tabbedPane.getTitleAt(currentIndex));
				editor.selectAll();
				editor.requestFocusInWindow();
				currentLength = editor.getText().length();
				currentDimension = editor.getPreferredSize();
				editor.setMinimumSize(currentDimension);
			}

			@Override
			public void stateChanged(final ChangeEvent e) {
//				renameTabTitle();
				cancelEditing();
			}
			
		}

		private static final long serialVersionUID = -7092855656003233391L;
		
		public AbstractJTabbedPaneWithTabTitleAdapterAndButtonTabComponent() {
			super();

			new TabTitleAdapter(this);
		}

		@Override
		public void addTab(final String title, final Component component) {
			this.addTab(title, null, component);
		}

		@Override
		public void addTab(final String title, final Icon icon, final Component component) {
			super.addTab(title, icon, component, "double-click to rename this tab (save changes by pressing ENTER key)");
			
			final int index = this.indexOfComponent(component);
			
			if (index != -1) {
				this.setTabComponentAt(index, new ButtonTabComponent(this));
				
				this.tabAdded(index, title, component);
			}
		}

		@Override
		public void addTab(final String title, final Icon icon, final Component component, final String tip) {
			throw new UnsupportedOperationException();
		}

		protected boolean isTabTitleValid(final String title) {
			return true;
		}

		@Override
		public void removeTabAt(final int index) throws IndexOutOfBoundsException {
			final String title = this.getTitleAt(index);
			
			final Component component = this.getComponentAt(index);
			
			super.removeTabAt(index);
			
			this.tabRemoved(index, title, component);
		}
		
		@Override
		public void setTitleAt(final int index, final String newTitle) throws IndexOutOfBoundsException {
			final String oldTitle = this.getTitleAt(index);
			
			final Component component = this.getComponentAt(index);
			
			super.setTitleAt(index, newTitle);
			
			if (!oldTitle.equals(newTitle)) {
				this.tabTitleChanged(index, oldTitle, newTitle, component);
			}
		}
		
		protected abstract void tabAdded(int index, String title, final Component component);
		
		protected abstract void tabRemoved(int index, String title, final Component component);

		protected abstract void tabTitleChanged(int index, String oldTitle, String newTitle, final Component component);

	}
	
	private static final Icon defaultAddInputPortButtonIcon = null;
	
	private static final String defaultAddInputPortButtonText = "Add input port";
	
	private static final String defaultAddInputPortButtonTip = null;
	
	private static final Icon defaultAddOutputPortButtonIcon = null;
	
	private static final String defaultAddOutputPortButtonText = "Add output port";
	
	private static final String defaultAddOutputPortButtonTip = null;
	
	private static final String defaultEmptyInputPortsText = "No input ports";
	
	private static final String defaultEmptyOutputPortsText = "No output ports";
	
	private static final String defaultInputPortName = "in";
	
	private static final Icon defaultInputPortsTabIcon = WorkbenchIcons.inputPortIcon;
	
	private static final String defaultInputPortsTabText = "Input ports";
	
	private static final String defaultInputPortsTabTip = null;
	
	private static final String defaultOutputPortName = "out";
	
	private static final Icon defaultOutputPortsTabIcon = WorkbenchIcons.outputPortIcon;

	private static final String defaultOutputPortsTabText = "Output ports";
	
	private static final String defaultOutputPortsTabTip = null;
	
	private static final long serialVersionUID = -3693674617612423237L;
	
	/**
	 * Returns the next port name with the specified <code>prefix</code>, given the set of existing <code>portNames</code>.
	 * 
	 * @param prefix  The prefix.  
	 * @param portNames  The set of existing port names. 
	 * @return  The next port name. 
	 * @throws IllegalArgumentException  If <code>prefix == null</code>.
	 */
	private static final String nextPortName(final String prefix, final Set<String> portNames) throws IllegalArgumentException {
		if (prefix == null) {
			throw new IllegalArgumentException(new NullPointerException("portName"));
		}
		
		final Set<Integer> indices = new HashSet<Integer>();
		
		if (portNames != null) {
			// Compile a regular expression for the prefix followed by an integer.
			final Pattern pattern = Pattern.compile(String.format("^\\s*?%s(\\d+)\\s*?$", Pattern.quote(prefix)));
			
			for (final String nextPortName : portNames) {
				final Matcher matcher = pattern.matcher(nextPortName);
				
				if (matcher.matches()) {
					// If the regular expression matches the 'nextPortName', then add the integer (capture group 1) to the set. 
					indices.add(Integer.parseInt(matcher.group(1)));
				}
			}
		}
		
		// The next index is the successor of the maximum index. 
		final int nextIndex = 1 + (indices.isEmpty() ? 0 : Collections.max(indices)); 
		
		return String.format("%s%d", prefix, nextIndex);
	}
	
	private final ACTIVITY activity;
	
	private CONFIG configBean;
	
	/**
	 * The label that is displayed when there are no input ports.
	 */
	private final JLabel emptyInputPortsLabel = new JLabel();
	
	/**
	 * The label that is displayed when there are no output ports.
	 */
	private final JLabel emptyOutputPortsLabel = new JLabel();
	
	/**
	 * The panel that contains either the tabbed pane of input ports, or the empty label. 
	 */
	private final JPanel inputPortsPane = new JPanel();
	
	/**
	 * The tabbed pane for input ports. 
	 */
	private final JTabbedPane inputPortsTabbedPane = new AbstractJTabbedPaneWithTabTitleAdapterAndButtonTabComponent() {

		private static final long serialVersionUID = 3161996013853282097L;

		@Override
		protected boolean isTabTitleValid(final String title) {
			return ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.isPortName(title);
		}

		@Override
		protected void tabAdded(final int index, final String title, final Component component) {
			// If the count is unity, then we have just added the first tab.  Hence,
			// we need to swap the contents of the pane, re-validate and re-paint.
			if (this.getTabCount() == 1) {
				inputPortsPane.remove(emptyInputPortsLabel);
				
				inputPortsPane.add(this, BorderLayout.CENTER);
				
				inputPortsPane.revalidate();
				inputPortsPane.repaint();
			}
			
			// If this component is not refreshing, then select the new tab. 
			if (!refreshing) {
				this.setSelectedIndex(index);
			}
			
			@SuppressWarnings("unchecked")
			final IN_COMPONENT inputPortComponent = (IN_COMPONENT) component;
			
			ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.inputPortAdded(title, inputPortComponent);
		}

		@Override
		protected void tabRemoved(final int index, final String title, final Component component) {
			// If the count is zero, then we have just removed the last tab.  Hence,
			// we need to swap the contents of the pane, re-validate and re-paint. 
			if (this.getTabCount() == 0) {
				inputPortsPane.remove(this);
				
				inputPortsPane.add(emptyInputPortsLabel, BorderLayout.CENTER);
				
				inputPortsPane.revalidate();
				inputPortsPane.repaint();
			}
			
			@SuppressWarnings("unchecked")
			final IN_COMPONENT inputPortComponent = (IN_COMPONENT) component;
			
			ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.inputPortRemoved(title, inputPortComponent);
		}

		@Override
		protected void tabTitleChanged(final int index, final String oldTitle, final String newTitle, final Component component) {
			@SuppressWarnings("unchecked")
			final IN_COMPONENT inputPortComponent = (IN_COMPONENT) component;
			
			ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.inputPortNameChanged(oldTitle, newTitle, inputPortComponent);
		}
		
	};
	
	/**
	 * The panel that contains either the tabbed pane of output ports, or the empty label. 
	 */
	private final JPanel outputPortsPane = new JPanel();
	
	/**
	 * The tabbed pane for output ports. 
	 */
	private final JTabbedPane outputPortsTabbedPane = new AbstractJTabbedPaneWithTabTitleAdapterAndButtonTabComponent() {

		private static final long serialVersionUID = -7885377287285294474L;

		@Override
		protected boolean isTabTitleValid(final String title) {
			return ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.isPortName(title);
		}

		@Override
		protected void tabAdded(final int index, final String title, final Component component) {
			// If the count is unity, then we have just added the first tab.  Hence,
			// we need to swap the contents of the pane, re-validate and re-paint.
			if (this.getTabCount() == 1) {
				outputPortsPane.remove(emptyOutputPortsLabel);
				
				outputPortsPane.add(this, BorderLayout.CENTER);
				
				outputPortsPane.revalidate();
				outputPortsPane.repaint();
			}
			
			// If this component is not refreshing, then select the new tab. 
			if (!refreshing) {
				this.setSelectedIndex(index);
			}
			
			@SuppressWarnings("unchecked")
			final OUT_COMPONENT outputPortComponent = (OUT_COMPONENT) component;
			
			ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.outputPortAdded(title, outputPortComponent);
		}

		@Override
		protected void tabRemoved(final int index, final String title, final Component component) {
			// If the count is zero, then we have just removed the last tab.  Hence,
			// we need to swap the contents of the pane, re-validate and re-paint. 
			if (this.getTabCount() == 0) {
				outputPortsPane.remove(this);
				
				outputPortsPane.add(emptyOutputPortsLabel, BorderLayout.CENTER);
				
				outputPortsPane.revalidate();
				outputPortsPane.repaint();
			}
			
			@SuppressWarnings("unchecked")
			final OUT_COMPONENT outputPortComponent = (OUT_COMPONENT) component;
			
			ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.outputPortRemoved(title, outputPortComponent);
		}

		@Override
		protected void tabTitleChanged(final int index, final String oldTitle, final String newTitle, final Component component) {
			@SuppressWarnings("unchecked")
			final OUT_COMPONENT outputPortComponent = (OUT_COMPONENT) component;
			
			ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.outputPortNameChanged(oldTitle, newTitle, outputPortComponent);
		}
		
	};
	
	/**
	 * Mutex variable that is used to synchronize "refreshConfiguration" method.
	 * <p>
	 * This class is a user-interface component.  Hence, it is not safe to synchronize
	 * on "this" instance, as it could block the thread[s] for the renderer.  Instead,
	 * we synchronize on a unique object.    
	 */
	private final Object refreshConfigurationLock = new Object();
	
	/**
	 * Flag that indicates whether or not this component is refreshing, i.e., the refreshConfiguration method is being executed.
	 * <p>
	 * This attribute is declared as "volatile" to ensure that it is accessed by a single thread at a time. 
	 */
	private volatile boolean refreshing = false;
	
	/**
	 * The "master" tabbed pane for this component. 
	 */
	private final JTabbedPane tabbedPane = new JTabbedPane();
	
	/**
	 * Sole constructor.
	 * 
	 * @param activity  The activity.
	 * @throws IllegalArgumentException  If <code>activity == null</code>.
	 */
	public ActivityConfigurationPanelWithInputPortAndOutputPortComponents(final ACTIVITY activity) throws IllegalArgumentException {
		super();
		
		if (activity == null) {
			throw new IllegalArgumentException(new NullPointerException("activity"));
		}
		
		this.activity = activity;
		
		// Call the "removeAll" method before the "initGui" method to ensure that this
		// panel contains no components before it is initialized. 
		this.removeAll();
		
		this.initGui();
		
		// Call the "refreshConfiguration" method after the "initGui" method to ensure
		// that this panel is initialized before the configuration bean is refreshed
		// for the first time. 
		this.refreshConfiguration();
	}

	/**
	 * Adds a <code>component</code> represented by a <code>title</code> and no icon. 
	 * 
	 * @param title  the title to be displayed in this tab
	 * @param component  the component to be displayed when this tab is clicked
	 */
	public final void addTab(final String title, final Component component) {
		this.addTab(title, null, component);
	}

	/**
	 * Adds a <code>component</code> represented by a <code>title</code> and/or <code>icon</code>, either of which can be <code>null</code>.
	 * 
	 * @param title  the title to be displayed in this tab
	 * @param icon  the icon to be displayed in this tab
	 * @param component  the component to be displayed when this tab is clicked
	 */
	public final void addTab(final String title, final Icon icon, final Component component) {
		this.addTab(title, icon, component, null);
	}
	
	/**
	 * Adds a <code>component</code> and <code>tip</code> represented by a <code>title</code> and/or <code>icon</code>, either of which can be <code>null</code>.
	 * 
	 * @param title  the title to be displayed in this tab
	 * @param icon  the icon to be displayed in this tab
	 * @param component  the component to be displayed when this tab is clicked
	 * @param tip  the tooltip to be displayed for this tab
	 */
	public final void addTab(final String title, final Icon icon, final Component component, final String tip) {
		final int index = tabbedPane.indexOfComponent(component);
		
		if (index == -1) {
			tabbedPane.addTab(title, icon, component, tip);
		} 
	}
	
	/**
	 * Validates the values of an input port component.
	 * 
	 * @param inputPortComponent  the input port component to be checked
	 * @return  <code>true</code> if the values are valid, otherwise, <code>false</code>.
	 */
	protected abstract boolean checkInputPortComponentValues(IN_COMPONENT inputPortComponent);
	
	/**
	 * Validates the values of an output port component.
	 * 
	 * @param inputPortComponent  the output port component to be checked
	 * @return  <code>true</code> if the values are valid, otherwise, <code>false</code>.
	 */
	protected abstract boolean checkOutputPortComponentValues(OUT_COMPONENT outputPortComponent);
	
	@Override
	public boolean checkValues() {
		// Check each input port component.
		for (final IN_COMPONENT inputPortComponent : this.getInputPortComponents().values()) {
			if (!this.checkInputPortComponentValues(inputPortComponent)) {
				return false;
			}
		}
		
		// Check each output port component.
		for (final OUT_COMPONENT outputPortComponent : this.getOutputPortComponents().values()) {
			if (!this.checkOutputPortComponentValues(outputPortComponent)) {
				return false;
			}
		}
		
		// Success!!
		return true;
	}
	
	/**
	 * Returns the activity for this configuration panel.
	 * 
	 * @return  The activity for this configuration panel.
	 * @throws IllegalStateException  If <code>activity == null</code>.
	 */
	public final ACTIVITY getActivity() throws IllegalStateException {
		if (activity == null) {
			throw new IllegalStateException(new NullPointerException("activity"));
		}
		
		return activity;
	}
	
	@Override
	public final CONFIG getConfiguration() {
		return configBean;
	}
	
	/**
	 * Returns the tab icon for the given <code>component</code>.
	 * 
	 * @param component  the component being queried
	 * @return  the icon for <code>component</code>
	 * @throws IndexOutOfBoundsException  if component is not found
	 */
	public final Icon getIcon(final Component component) throws IndexOutOfBoundsException {
		final int index = tabbedPane.indexOfComponent(component);
		
		return tabbedPane.getIconAt(index);
	}
	
	/**
	 * Returns an immutable map of port names to input port components. 
	 * <p>
	 * Equivalent to: <code>this.getInputPortComponents(Cardinality.MANY);</code>
	 * 
	 * @return  an immutable map of port names to input port components.
	 */
	public final Map<String, IN_COMPONENT> getInputPortComponents() {
		return this.getInputPortComponents(Cardinality.MANY);
	}
	
	/**
	 * Returns an immutable map of port names to input port components, restricted by the given <code>cardinality</code>.
	 * <p>
	 * The behavior of this method is controlled by the <code>cardinality</code> parameter:
	 * <ul>
	 *   <li><code>Cardinality.ZERO</code> - Map is empty</li>
	 *   <li><code>Cardinality.ONE</code> - Map contains only selected input port component</li>
	 *   <li><code>Cardinality.MANY</code> - Map contains all input port components</li>
	 * </ul>
	 * 
	 * @param cardinality  the cardinality
	 * @return  an immutable map of port names to input port components.
	 */
	public final Map<String, IN_COMPONENT> getInputPortComponents(final Cardinality cardinality) {
		final Map<String, IN_COMPONENT> inputPortComponents = new TreeMap<String, IN_COMPONENT>();
		
		switch (cardinality) {
		case ZERO:
			break;
		case ONE:
			final int selectedIndex = inputPortsTabbedPane.getSelectedIndex();
			
			if (selectedIndex != -1) {
				final String portName = inputPortsTabbedPane.getTitleAt(selectedIndex);
				
				@SuppressWarnings("unchecked")
				final IN_COMPONENT inputPortComponent = (IN_COMPONENT) inputPortsTabbedPane.getComponentAt(selectedIndex);
				
				inputPortComponents.put(portName, inputPortComponent);
			}
			
			break;
		case MANY:
			for (int index = 0, length = inputPortsTabbedPane.getTabCount(); index < length; index++) {
				final String portName = inputPortsTabbedPane.getTitleAt(index);
				
				@SuppressWarnings("unchecked")
				final IN_COMPONENT inputPortComponent = (IN_COMPONENT) inputPortsTabbedPane.getComponentAt(index);
				
				inputPortComponents.put(portName, inputPortComponent);
			}
			
			break;
		default:
			break;
		}
		
		return Collections.unmodifiableMap(inputPortComponents);
	}
	
	/**
	 * Returns a map of port names to input port configuration beans for the given <code>configBean</code>. 
	 * 
	 * @param configBean  the activity configuration bean
	 * @return  map of port names to input port configuration beans.
	 */
	protected abstract Map<String, IN_CONFIG> getInputPortConfigurations(CONFIG configBean);
	
	/**
	 * Returns an immutable map of port names to output port components. 
	 * <p>
	 * Equivalent to: <code>this.getOutputPortComponents(Cardinality.MANY);</code>
	 * 
	 * @return  an immutable map of port names to output port components.
	 */
	public final Map<String, OUT_COMPONENT> getOutputPortComponents() {
		return this.getOutputPortComponents(Cardinality.MANY);
	}
	
	/**
	 * Returns an immutable map of port names to output port components, restricted by the given <code>cardinality</code>.
	 * <p>
	 * The behavior of this method is controlled by the <code>cardinality</code> parameter:
	 * <ul>
	 *   <li><code>Cardinality.ZERO</code> - Map is empty</li>
	 *   <li><code>Cardinality.ONE</code> - Map contains only selected output port component</li>
	 *   <li><code>Cardinality.MANY</code> - Map contains all output port components</li>
	 * </ul>
	 * 
	 * @param cardinality  the cardinality
	 * @return  an immutable map of port names to output port components.
	 */
	public final Map<String, OUT_COMPONENT> getOutputPortComponents(final Cardinality cardinality) {
		final Map<String, OUT_COMPONENT> outputPortComponents = new TreeMap<String, OUT_COMPONENT>();
		
		switch (cardinality) {
		case ZERO:
			break;
		case ONE:
			final int selectedIndex = outputPortsTabbedPane.getSelectedIndex();
			
			if (selectedIndex != -1) {
				final String portName = outputPortsTabbedPane.getTitleAt(selectedIndex);
				
				@SuppressWarnings("unchecked")
				final OUT_COMPONENT outputPortComponent = (OUT_COMPONENT) outputPortsTabbedPane.getComponentAt(selectedIndex);
				
				outputPortComponents.put(portName, outputPortComponent);
			}
			
			break;
		case MANY:
			for (int index = 0, length = outputPortsTabbedPane.getTabCount(); index < length; index++) {
				final String portName = outputPortsTabbedPane.getTitleAt(index);
				
				@SuppressWarnings("unchecked")
				final OUT_COMPONENT outputPortComponent = (OUT_COMPONENT) outputPortsTabbedPane.getComponentAt(index);
				
				outputPortComponents.put(portName, outputPortComponent);
			}
			
			break;
		default:
			break;
		}
		
		return Collections.unmodifiableMap(outputPortComponents);
	}
	
	/**
	 * Returns a map of port names to output port configuration beans for the given <code>configBean</code>. 
	 * 
	 * @param configBean  the activity configuration bean
	 * @return  map of port names to output port configuration beans.
	 */
	protected abstract Map<String, OUT_CONFIG> getOutputPortConfigurations(CONFIG configBean);
	
	/**
	 * Returns the tab title for the given <code>component</code>.
	 * 
	 * @param component  the component being queried
	 * @return  the title for <code>component</code>
	 * @throws IndexOutOfBoundsException  if component is not found
	 */
	public final String getTitle(final Component component) throws IndexOutOfBoundsException {
		final int index = tabbedPane.indexOfComponent(component);
		
		return tabbedPane.getTitleAt(index);
	}
	
	/**
	 * Returns the tooltip text for the given <code>component</code>.
	 * 
	 * @param component  the component being queried
	 * @return  the tooltip text for <code>component</code>
	 * @throws IndexOutOfBoundsException  if component is not found
	 */
	public final String getToolTipText(final Component component) throws IndexOutOfBoundsException {
		final int index = tabbedPane.indexOfComponent(component);
		
		return tabbedPane.getToolTipTextAt(index);
	}
	
	/**
	 * Initializes the graphical user interface (GUI).
	 * <p>
	 * Subclasses may refine the implementation of this method. 
	 */
	protected void initGui() {
		this.setDoubleBuffered(false);
		this.setLayout(new BorderLayout());
		
		tabbedPane.setBorder(BorderFactory.createEmptyBorder());
		
		tabbedPane.addTab(defaultInputPortsTabText, defaultInputPortsTabIcon, inputPortsPane, defaultInputPortsTabTip);
		tabbedPane.addTab(defaultOutputPortsTabText, defaultOutputPortsTabIcon, outputPortsPane, defaultOutputPortsTabTip);
		
		this.add(tabbedPane, BorderLayout.CENTER);
		
		inputPortsPane.setBorder(BorderFactory.createEmptyBorder());
		inputPortsPane.setLayout(new BorderLayout());
		
		emptyInputPortsLabel.setHorizontalAlignment(JLabel.CENTER);
		emptyInputPortsLabel.setText(defaultEmptyInputPortsText);
		
		final JButton addInputPortButton = new JButton();
		addInputPortButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				final String portName = nextPortName(defaultInputPortName, ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.getInputPortComponents().keySet());
				
				final IN_CONFIG inputPortConfigBean = ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.newInputPortConfiguration();
				
				final IN_COMPONENT inputPortComponent = ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.toInputPortComponent(portName, inputPortConfigBean);
				
				inputPortsTabbedPane.addTab(portName, inputPortComponent);
			}
			
		});
		addInputPortButton.setIcon(defaultAddInputPortButtonIcon);
		addInputPortButton.setText(defaultAddInputPortButtonText);
		addInputPortButton.setToolTipText(defaultAddInputPortButtonTip);
		
		inputPortsPane.add(emptyInputPortsLabel, BorderLayout.CENTER);
		inputPortsPane.add(addInputPortButton, BorderLayout.SOUTH);
		
		outputPortsPane.setBorder(BorderFactory.createEmptyBorder());
		outputPortsPane.setLayout(new BorderLayout());
		
		emptyOutputPortsLabel.setHorizontalAlignment(JLabel.CENTER);
		emptyOutputPortsLabel.setText(defaultEmptyOutputPortsText);
		
		final JButton addOutputPortButton = new JButton();
		addOutputPortButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) { 
				final String portName = nextPortName(defaultOutputPortName, ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.getOutputPortComponents().keySet());
				
				final OUT_CONFIG outputPortConfigBean = ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.newOutputPortConfiguration();
				
				final OUT_COMPONENT outputPortComponent = ActivityConfigurationPanelWithInputPortAndOutputPortComponents.this.toOutputPortComponent(portName, outputPortConfigBean);
				
				outputPortsTabbedPane.addTab(portName, outputPortComponent);
			}
			
		});
		addOutputPortButton.setIcon(defaultAddOutputPortButtonIcon);
		addOutputPortButton.setText(defaultAddOutputPortButtonText);
		addOutputPortButton.setToolTipText(defaultAddOutputPortButtonTip);
		
		outputPortsPane.add(emptyOutputPortsLabel, BorderLayout.CENTER);
		outputPortsPane.add(addOutputPortButton, BorderLayout.SOUTH);
	}
	
	/**
	 * Called when an input port tab is added.
	 * 
	 * @param portName  the title of the tab
	 * @param inputPortComponent  the component
	 */
	protected void inputPortAdded(final String portName, final IN_COMPONENT inputPortComponent) {
		return;
	}
	
	/**
	 * Called when the title of an input port tab is changed.
	 * 
	 * @param oldPortName  the original title of the tab
	 * @param newPortName  the new title of the tab
	 * @param inputPortComponent  the component
	 */
	protected void inputPortNameChanged(final String oldPortName, final String newPortName, final IN_COMPONENT inputPortComponent) {
		return;
	}
	
	/**
	 * Called when an input port tab is removed.
	 * 
	 * @param portName  the title of the tab
	 * @param inputPortComponent  the component
	 */
	protected void inputPortRemoved(final String portName, final IN_COMPONENT inputPortComponent) {
		return;
	}
	
	@Override
	public final boolean isConfigurationChanged() {
		// This line assumes that the configuration bean implements the Object#equals(Object) method.
		return !ObjectUtils.equals(this.getConfiguration(), this.toConfiguration());
	}
	
	/**
	 * Tests if the given <code>input</code> contains a valid port name.
	 * 
	 * @param input  the character sequence
	 * @return  <code>true</code> if the <code>input</code> contains a valid port name, otherwise, <code>false</code>.
	 */
	protected boolean isPortName(final CharSequence input) {
		return Pattern.matches("^\\w+$", input);
	}
	
	/**
	 * Returns a new activity configuration bean.
	 * 
	 * @return a new activity configuration bean
	 */
	protected abstract CONFIG newConfiguration();
	
	/**
	 * Returns a new input port configuration bean.
	 * 
	 * @return a new input port configuration bean
	 */
	protected abstract IN_CONFIG newInputPortConfiguration();
	
	/**
	 * Returns a new output port configuration bean.
	 * 
	 * @return a new output port configuration bean
	 */
	protected abstract OUT_CONFIG newOutputPortConfiguration();
	
	@Override
	public final void noteConfiguration() {
		configBean = this.toConfiguration();
	}
	
	/**
	 * Called when an output port tab is added.
	 * 
	 * @param portName  the title of the tab
	 * @param outputPortComponent  the component
	 */
	protected void outputPortAdded(final String portName, final OUT_COMPONENT outputPortComponent) {
		return;
	}
	
	/**
	 * Called when the title of an output port tab is changed.
	 * 
	 * @param oldPortName  the original title of the tab
	 * @param newPortName  the new title of the tab
	 * @param outputPortComponent  the component
	 */
	protected void outputPortNameChanged(final String oldPortName, final String newPortName, final OUT_COMPONENT outputPortComponent) {
		return;
	}
	
	/**
	 * Called when an output port tab is removed.
	 * 
	 * @param portName  the title of the tab
	 * @param outputPortComponent  the component
	 */
	protected void outputPortRemoved(final String portName, final OUT_COMPONENT outputPortComponent) {
		return;
	}
	
	@Override
	public void refreshConfiguration() {
		if (refreshing) {
			// If we're already refreshing, then there is no work to do. 
			return;
		}
		
		synchronized (refreshConfigurationLock) {
			if (refreshing) {
				// Re-test the flag; defensive programming against race conditions. If we're
				// already refreshing, then something has gone wrong...
				throw new IllegalStateException("refreshing");
			} else {
				// Otherwise, raise the flag. 
				refreshing = true;
			}
			
			try {
				inputPortsTabbedPane.removeAll();
				outputPortsTabbedPane.removeAll();
				
				configBean = this.getActivity().getConfiguration();
				
				final Map<String, IN_CONFIG> inputPortConfigBeans = this.getInputPortConfigurations(configBean);
				
				if (inputPortConfigBeans != null) {
					for (final Map.Entry<String, IN_CONFIG> entry : (new TreeMap<String, IN_CONFIG>(inputPortConfigBeans)).entrySet()) {
						final String portName = entry.getKey();
						
						final IN_CONFIG inputPortConfigBean = entry.getValue();
						
						final IN_COMPONENT inputPortComponent = this.toInputPortComponent(portName, inputPortConfigBean);
						
						inputPortsTabbedPane.addTab(portName, inputPortComponent);
					}
				}
				
				final Map<String, OUT_CONFIG> outputPortConfigBeans = this.getOutputPortConfigurations(configBean);
				
				if (outputPortConfigBeans != null) {
					for (final Map.Entry<String, OUT_CONFIG> entry : (new TreeMap<String, OUT_CONFIG>(outputPortConfigBeans)).entrySet()) {
						final String portName = entry.getKey();
						
						final OUT_CONFIG outputPortConfigBean = entry.getValue();
						
						final OUT_COMPONENT outputPortComponent = this.toOutputPortComponent(portName, outputPortConfigBean);
						
						outputPortsTabbedPane.addTab(portName, outputPortComponent);
					}
				}
			} finally {
				// Ensure that the flag is lowered, even if an exception is raised inside the "try" block. 
				refreshing = false;
			}
		}
	}
	
	/**
	 * Removes the tab for the specified <code>component</code>
	 * 
	 * @param component  the component whose tab is to be removed
	 * @throws IndexOutOfBoundsException  if component is not found
	 */
	public final void removeTab(final Component component) throws IndexOutOfBoundsException {
		final int index = tabbedPane.indexOfComponent(component);
		
		tabbedPane.removeTabAt(index);
	}
	
	/**
	 * Sets the icon for the <code>component</code> to <code>icon</code>, which may be <code>null</code>.
	 * 
	 * @param component  the component whose icon is to be set
	 * @param icon  the icon to be displayed in the tab
	 * @throws IndexOutOfBoundsException  if component is not found
	 */
	public final void setIcon(final Component component, final Icon icon) throws IndexOutOfBoundsException {
		final int index = tabbedPane.indexOfComponent(component);
		
		tabbedPane.setIconAt(index, icon);
	}
	
	/**
	 * Sets the map of port names to input port configuration beans for the given <code>configBean</code>. 
	 * 
	 * @param configBean  the activity configuration bean
	 * @param inputPortConfigBeans  the new map of port names to input port configuration beans
	 */
	protected abstract void setInputPortConfigurations(CONFIG configBean, Map<String, IN_CONFIG> inputPortConfigBeans);
	
	/**
	 * Sets the map of port names to output port configuration beans for the given <code>configBean</code>. 
	 * 
	 * @param configBean  the activity configuration bean
	 * @param outputPortConfigBeans  the new map of port names to input port configuration beans
	 */
	protected abstract void setOutputPortConfigurations(CONFIG configBean, Map<String, OUT_CONFIG> outputPortConfigBeans);

	/**
	 * Sets the title for the <code>component</code> to <code>title</code>.
	 * 
	 * @param component  the component whose title is to be set
	 * @param title  the title to be displayed in the tab
	 * @throws IndexOutOfBoundsException  if component is not found
	 */
	public final void setTitle(final Component component, final String title) throws IndexOutOfBoundsException {
		final int index = tabbedPane.indexOfComponent(component);
		
		tabbedPane.setTitleAt(index, title);
	}
	
	/**
	 * Sets the tooltip text for the <code>component</code> to <code>tip</code>, which may be <code>null</code>.
	 * 
	 * @param component  the component whose title is to be set
	 * @param tip  the tooltip text to be displayed in the tab
	 * @throws IndexOutOfBoundsException  if component is not found
	 */
	public final void setToolTipText(final Component component, final String tip) throws IndexOutOfBoundsException {
		final int index = tabbedPane.indexOfComponent(component);
		
		tabbedPane.setToolTipTextAt(index, tip);
	}
	
	/**
	 * Returns this configuration panel as an activity configuration bean.
	 * <p>
	 * Equivalent to: <code>this.toConfiguration(Cardinality.MANY, Cardinality.MANY);</code>
	 * 
	 * @return  an activity configuration bean
	 */
	public final CONFIG toConfiguration() {
		return this.toConfiguration(Cardinality.MANY, Cardinality.MANY);
	}
	
	/**
	 * Returns this configuration panel as an activity configuration bean, whose input and output ports are limited by the specified cardinalities. 
	 * 
	 * @param inputPortCardinality  the cardinality for the selection of input ports
	 * @param outputPortCardinality  the cardinality for the selection of output ports
	 * @return  an activity configuration bean
	 * @see #getInputPortComponents(Cardinality)
	 * @see #getOutputPortComponents(Cardinality)
	 */
	public CONFIG toConfiguration(final Cardinality inputPortCardinality, final Cardinality outputPortCardinality) {
		final CONFIG configBean = this.newConfiguration();
		
		final Map<String, IN_CONFIG> inputPortConfigBeans = new TreeMap<String, IN_CONFIG>();
		
		for (final Map.Entry<String, IN_COMPONENT> entry : this.getInputPortComponents(inputPortCardinality).entrySet()) {
			final String portName = entry.getKey();
			
			final IN_COMPONENT inputPortComponent = entry.getValue();
			
			final IN_CONFIG inputPortConfigBean = this.toInputPortConfiguration(portName, inputPortComponent);
			
			inputPortConfigBeans.put(portName, inputPortConfigBean);
		}
		
		this.setInputPortConfigurations(configBean, Collections.unmodifiableMap(inputPortConfigBeans));
		
		final Map<String, OUT_CONFIG> outputPortConfigBeans = new TreeMap<String, OUT_CONFIG>();
		
		for (final Map.Entry<String, OUT_COMPONENT> entry : this.getOutputPortComponents(outputPortCardinality).entrySet()) {
			final String portName = entry.getKey();
			
			final OUT_COMPONENT outputPortComponent = entry.getValue();
			
			final OUT_CONFIG outputPortConfigBean = this.toOutputPortConfiguration(portName, outputPortComponent);
			
			outputPortConfigBeans.put(portName, outputPortConfigBean);
		}
		
		this.setOutputPortConfigurations(configBean, Collections.unmodifiableMap(outputPortConfigBeans));
		
		return configBean;
	}

	/**
	 * Returns the given input port configuration bean as an input port component.
	 * 
	 * @param portName  the port name
	 * @param inputPortConfigBean  the input port configuration bean
	 * @return  the input port component
	 */
	protected abstract IN_COMPONENT toInputPortComponent(String portName, IN_CONFIG inputPortConfigBean);

	/**
	 * Returns the given input port component as an input port configuration bean.
	 * 
	 * @param portName  the port name
	 * @param inputPortComponent  the input port component
	 * @return  the input port configuration bean
	 */
	protected abstract IN_CONFIG toInputPortConfiguration(String portName, IN_COMPONENT inputPortComponent);
	
	/**
	 * Returns the given output port configuration bean as an output port component.
	 * 
	 * @param portName  the port name
	 * @param outputPortConfigBean  the output port configuration bean
	 * @return  the output port component
	 */
	protected abstract OUT_COMPONENT toOutputPortComponent(String portName, OUT_CONFIG outputPortConfigBean);
	
	/**
	 * Returns the given output port component as an output port configuration bean.
	 * 
	 * @param portName  the port name
	 * @param outputPortComponent  the output port component
	 * @return  the output port configuration bean
	 */
	protected abstract OUT_CONFIG toOutputPortConfiguration(String portName, OUT_COMPONENT outputPortComponent);

}
