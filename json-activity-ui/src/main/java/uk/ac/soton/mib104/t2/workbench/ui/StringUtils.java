package uk.ac.soton.mib104.t2.workbench.ui;

/**
 * Utility methods for strings. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class StringUtils {

	/**
	 * Returns the <code>plural</code> word, unless <code>count</code> is 1 or -1. 
	 * In which case, the <code>singular</code> word is returned. 
	 * 
	 * @param count  The magnitude.
	 * @param singular  The singular. 
	 * @param plural  The plural.  
	 * @return  <code>(count == 1 || count == -1) ? singular : plural</code>
	 */
	public static final String pluralize(final int count, final String singular, final String plural) {
		switch (count) {
		case -1:
		case 1:
			return String.format(singular, count);
		default:
			return String.format(plural, count);
		}
	}
	
	/**
	 * Sole constructor.
	 */
	private StringUtils() {
		super();
	}
	
}
