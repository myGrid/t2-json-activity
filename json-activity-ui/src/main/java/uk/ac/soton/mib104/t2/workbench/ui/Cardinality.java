package uk.ac.soton.mib104.t2.workbench.ui;

/**
 * Enumeration of cardinality magnitudes (zero, one and many). 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public enum Cardinality {

	MANY("*"),
	ONE("1"),
	ZERO("0"),
	;
	
	private final String string;
	
	/**
	 * Sole constructor.
	 * 
	 * @param string  The human-readable form of this cardinality. 
	 */
	private Cardinality(final String string) {
		if (string == null) {
			throw new IllegalArgumentException(new NullPointerException("string"));
		}
		
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
}
