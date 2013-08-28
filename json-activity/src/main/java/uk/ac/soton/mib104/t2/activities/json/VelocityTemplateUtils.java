/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.velocity.Template;
import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ASTSetDirective;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.visitor.BaseVisitor;

/**
 * Utility methods for Apache Velocity templates. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 * @see <a href="http://velocity.apache.org/engine/releases/velocity-1.7/user-guide.html">Velocity User Guide</a>
 */
public final class VelocityTemplateUtils {
	
	/**
	 * The names of Apache Velocity directives that introduce new variables.  
	 */
	private static final Set<String> directiveNames = new HashSet<String>(Arrays.asList("define", "foreach"));
	
	/**
	 * Returns the set of <b>all</b> declared variable names for the given <code>template</code>.
	 * <p>
	 * Equivalent to: <code>getVariableNames(template, false);</code>
	 * 
	 * @param template  The template.
	 * @return  The set of <b>all</b> declared variable names. 
	 */
	public static final Set<String> getVariableNames(final Template template) {
		return getVariableNames(template, false);
	}
	
	/**
	 * Returns the set of declared variable names for the given <code>template</code>.
	 * 
	 * @param template  The template. 
	 * @param onlyDirectives  If <code>true</code>, then returns variable names that are
	 *                        declared as part of a #directive.  Otherwise, returns <b>all</b>
	 *                        variable names. 
	 * @return  The set of declared variable names. 
	 */
	public static final Set<String> getVariableNames(final Template template, final boolean onlyDirectives) {
		// Create a container for the results.
		final Set<String> results = new HashSet<String>();
		
		// If the template is undefined, then ensure that the container has no elements.
		if (template != null) {
			if (onlyDirectives) {
				// If the 'onlyDirectives' flag is true, then create an AST visitor for #directives.
				((SimpleNode) template.getData()).jjtAccept(new BaseVisitor() {

					@Override
					public Object visit(final ASTDirective node, final Object data) {
						if (directiveNames.contains(node.getDirectiveName())) {
							final SimpleNode childNode = (SimpleNode) node.jjtGetChild(0);
							
							if (childNode instanceof ASTReference) {
								// If the name of the given directive 'node' is an element of the 'directiveNames'
								// static container and the first 'childNode' is an AST reference, then add the root
								// string of said 'childNode' to the 'results' container. 
								results.add(((ASTReference) childNode).getRootString());
							}
						}
						
						return super.visit(node, data);
					}

					@Override
					public Object visit(final ASTSetDirective node, final Object data) {
						final SimpleNode childNode = (SimpleNode) node.jjtGetChild(0);
						
						if (childNode instanceof ASTReference) {
							// If the first 'childNode' is an AST reference, then add the root string of said
							// 'childNode' to the 'results' container. 
							results.add(((ASTReference) childNode).getRootString());
						}
						
						return super.visit(node, data);
					}
					
				}, null);
			} else {
				// If the 'onlyDirectives' flag is false, then create an AST visitor for references only.
				((SimpleNode) template.getData()).jjtAccept(new BaseVisitor() {

					@Override
					public Object visit(final ASTReference node, final Object referenceData) {
						// Add the root string to the 'results' container. 
						results.add(node.getRootString());
						
						return super.visit(node, referenceData);
					}
					
				}, null);
			}
		}
		
		// Ensure that the 'results' container is immutable.
		return Collections.unmodifiableSet(results);
	}
	
	/**
	 * Returns <code>true</code> if the given <code>input</code> is a valid variable name, otherwise, returns <code>false</code>.
	 * 
	 * @param input  The character sequence.  
	 * @return  <code>true</code> if <code>input</code> is a valid variable name, otherwise, <code>false</code>. 
	 */
	public static final boolean isVariableName(final CharSequence input) {
		return Pattern.matches("^([a-zA-Z][a-zA-Z0-9_]*)$", input);
	}
	
	/**
	 * Returns the given <code>input</code> formatted as an AST reference literal. 
	 * 
	 * @param input  The character sequence. 
	 * @param formalReferenceNotation  Use formal reference notation.
	 * @return  The <code>input</code> formatted as an AST reference literal.
	 * @throws IllegalArgumentException  If <code>input == null</code>.
	 */
	public static final String toASTReferenceLiteral(final CharSequence input, final boolean formalReferenceNotation) throws IllegalArgumentException {
		if (input == null) {
			throw new IllegalArgumentException(new NullPointerException("cs"));
		}
		
		return String.format(formalReferenceNotation ? "${%s}" : "$%s", input);
	}
	
	/**
	 * Sole constructor.
	 */
	private VelocityTemplateUtils() {
		super();
	}

}
