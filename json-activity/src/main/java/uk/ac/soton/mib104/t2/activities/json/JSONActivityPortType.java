/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONAware;
import net.minidev.json.JSONObject;

/**
 * Enumeration of JSON activity port types. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public enum JSONActivityPortType {
	
	JSON("JSON", Collections.unmodifiableList(Arrays.asList("application/json"))),
	TEXT("Text", Collections.unmodifiableList(Arrays.asList("text/plain"))),
	;
	
	private final List<String> mimeTypes;
	
	private final String name;

	/**
	 * Sole constructor.
	 * 
	 * @param name  The name of this port type.
	 */
	private JSONActivityPortType(final String name, final List<String> mimeTypes) {
		this.name = name;
		this.mimeTypes = mimeTypes;
	}
	
	/**
	 * The list of acceptable MIME types for this port type. 
	 * 
	 * @return  The list of acceptable MIME types for this port type. 
	 */
	public List<String> getMimeTypes() {
		return mimeTypes;
	}
	
	/**
	 * The name of this port type.
	 * 
	 * @return  The name of this port type.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Parse the specified input as JSON, or throw an exception. 
	 * 
	 * @param value  The input.
	 * @return  The input as JSON.
	 * @throws Throwable   
	 */
	public Object parse(final Object value) throws Throwable {
		if (value == null) {
			return null;
		} else if (value instanceof List) {
			final JSONArray output = new JSONArray();
			
			for (final Object e : (List<?>) value) {
				output.add(this.parse(e));
			}
			
			return output;
		} else if (value instanceof Map) {
			final JSONObject output = new JSONObject();
			
			for (final Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
				output.put(entry.getKey().toString(), this.parse(entry.getValue()));
			}
			
			return output;
		} else {
			switch (this) {
			case TEXT:
				return value.toString();
			case JSON:
				if (value instanceof JSONAware) {
					return value;
				} else {
					return JSONStringUtils.parseJSONString(value.toString());
				}
			default:
				return null;
			}
		}
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
}
