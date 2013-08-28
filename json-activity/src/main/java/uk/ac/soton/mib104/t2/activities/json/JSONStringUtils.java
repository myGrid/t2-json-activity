/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minidev.json.JSONValue;

/**
 * Utility methods for JSON string reading and writing. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONStringUtils {
	
	/**
	 * Default indentation for formatted JSON strings (2 spaces). 
	 */
	private static final String defaultIndentation = "  ";
	
	/**
	 * Append the formatted JSON string representation of the given <code>jsonValue</code> to the given string buffer <code>sb</code>. 
	 * 
	 * @param jsonValue  The JSON value.
	 * @param sb  The string buffer.
	 * @param indent  The level of indentation.
	 * @param indentPrefix  If <code>true</code>, then the first line will be prefixed with the specified level of indentation.
	 * @throws IllegalArgumentException  If <code>sb == null</code>.
	 */
	private static final void appendFormattedString(final Object jsonValue, final StringBuffer sb, final int indent, final boolean indentPrefix) throws IllegalArgumentException {
		if (sb == null) {
			throw new IllegalArgumentException("sb");
		}
		
		if (indentPrefix) {
			appendIndentation(sb, indent);
		}
		
		if (jsonValue == null) {
			sb.append(toJSONString(jsonValue, false));
		} else if (jsonValue instanceof List) {
			sb.append('[');
			
			boolean seenAtLeastOneElement = false;
			
			for (final Object e : (List<?>) jsonValue) {
				if (seenAtLeastOneElement) {
					sb.append(',');
				} else {
					seenAtLeastOneElement = true;
				}
				
				sb.append('\n');
				appendFormattedString(e, sb, indent + 1, true);
			}
			
			sb.append('\n');
			appendIndentation(sb, indent);
			sb.append(']');
		} else if (jsonValue instanceof Map) {
			sb.append('{');
			
			boolean seenAtLeastOneEntry = false;
			
			for (final Map.Entry<?, ?> entry : (new TreeMap<Object, Object>((Map<?, ?>) jsonValue)).entrySet()) {
				if (seenAtLeastOneEntry) {
					sb.append(',');
				} else {
					seenAtLeastOneEntry = true;
				}
				
				sb.append('\n');
				appendFormattedString(entry.getKey(), sb, indent + 1, true);
				sb.append(':');
				sb.append(' ');
				appendFormattedString(entry.getValue(), sb, indent + 1, false);
			}
			
			sb.append('\n');
			appendIndentation(sb, indent);
			sb.append('}');
		} else {
			sb.append(toJSONString(jsonValue, false));
		}
	}
	
	/**
	 * Append <code>n</code> levels of indentation to the given string buffer <code>sb</code>.
	 * 
	 * @param sb  The string buffer.
	 * @param n  The level of indentation. 
	 * @throws IllegalArgumentException  If <code>sb == null</code>.
	 */
	private static final void appendIndentation(final StringBuffer sb, final int n) throws IllegalArgumentException {
		if (sb == null) {
			throw new IllegalArgumentException("sb");
		}
		
		for (int index = 0; index < n; index++) {
			sb.append(defaultIndentation);
		}
	}
	
	/**
	 * Reads the given JSON string.
	 * 
	 * @param jsonString  The JSON string.
	 * @return  The result of the parse.
	 * @throws Throwable  If the JSON string is not well-formed.
	 */
	public static final Object parseJSONString(final String jsonString) throws Throwable {
		return JSONValue.parseStrict(jsonString);
	}
	
	/**
	 * Writes a single JSON value to a string.
	 * 
	 * @param jsonValue  The JSON value.
	 * @param formatted  Specifies if the JSON string should be formatted, i.e., pretty-printed. 
	 * @return  The JSON string. 
	 */
	public static final String toJSONString(final Object jsonValue, final boolean formatted) {
		if (formatted) {
			final StringBuffer sb = new StringBuffer();
			
			appendFormattedString(jsonValue, sb, 0, false);
			
			return sb.toString();
		} else {
			return JSONValue.toJSONString(jsonValue);
		}
	}
	
	/**
	 * Writes a list of JSON values to a list of strings. 
	 * 
	 * @param jsonValues  The list of JSON values.
	 * @param formatted  Specifies if the JSON strings should be formatted, i.e., pretty-printed. 
	 * @return  The list of JSON strings. 
	 */
	public static final List<Object> toJSONStringList(final List<?> jsonValues, final boolean formatted) {
		final List<Object> result = new ArrayList<Object>((jsonValues == null) ? 0 : ((List<?>) jsonValues).size());
		
		for (final Object jsonValue : jsonValues) {
			if (jsonValue == null) {
				result.add(toJSONString(jsonValue, formatted));
			} else if (jsonValue instanceof List) {
				result.add(toJSONStringList((List<?>) jsonValue, formatted));
			} else {
				result.add(toJSONString(jsonValue, formatted));
			}
		}
		
		return Collections.unmodifiableList(result);
	}
	
	/**
	 * Sole constructor.
	 */
	private JSONStringUtils() {
		super();
	}

}
