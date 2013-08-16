package com.famfamfam;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * FamFamFam Silk icons.
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 * @see <a href="http://www.famfamfam.com/lab/icons/silk/">famfamfam.com - Silk Icons</a>
 */
public final class Silk {
	
	/**
	 * Cache of icons (accessed in this thread.)
	 */
	private static final Map<String, Icon> icons = new HashMap<String, Icon>();
	
	public static final Icon getAcceptIcon() {
		return getIcon("accept");
	}
	
	public static final Icon getArrowLeftIcon() {
		return getIcon("arrow_left");
	}
	
	public static final Icon getArrowRightIcon() {
		return getIcon("arrow_right");
	}
	
	public static final Icon getChartOrganisationDeleteIcon() {
		return getIcon("chart_organisation_delete");
	}
	
	public static final Icon getExclamationIcon() {
		return getIcon("exclamation");
	}

	public static final Icon getHelpIcon() {
		return getIcon("help");
	}
	
	/**
	 * Returns a new icon; backed by the image with the given <code>name</code>.
	 * 
	 * @param name  The name. 
	 * @return  The icon.
	 */
	private static final Icon getIcon(final String name) {
		if (icons.containsKey(name)) {
			// If the cache already contains a match, then return said match.
			return icons.get(name);
		} else {
			final Icon icon = new ImageIcon(Silk.class.getResource(String.format("/famfamfam/silk/%s.png", name)));
			
			// Otherwise, add a new icon to the cache. 
			icons.put(name, icon);
			
			return icon;
		} 
	}
	
	public static final Icon getMagnifierIcon() {
		return getIcon("magnifier");
	}
	
	public static final Icon getPageWhiteDeleteIcon() {
		return getIcon("page_white_delete");
	}
	
	/**
	 * Sole constructor. 
	 */
	private Silk() {
		super();
	}
	
}
