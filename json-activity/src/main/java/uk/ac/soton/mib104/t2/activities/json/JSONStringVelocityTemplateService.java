/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

/**
 * Apache Velocity-based template service that returns JSON-encoded strings. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 * @see JSONValueVelocityTemplateService
 */
public final class JSONStringVelocityTemplateService extends AbstractVelocityTemplateService<String> {
	
	private static final class SingletonWrapper {
		
		private static AbstractVelocityTemplateService<String> INSTANCE;
		
		public static final AbstractVelocityTemplateService<String> getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new JSONStringVelocityTemplateService();
			}
			
			return INSTANCE;
		}
		
	}
	
	public static final AbstractVelocityTemplateService<String> getInstance() {
		return SingletonWrapper.getInstance();
	}
	
	/**
	 * Sole constructor. 
	 */
	private JSONStringVelocityTemplateService() {
		super();
	}
	
	@Override
	protected Object proxyInput(final Object obj) throws Throwable {
		// Delegate to the JSON value template service. 
		return JSONValueVelocityTemplateService.getInstance().proxyInput(obj);
	}

	@Override
	protected String proxyOutput(final String t) throws Throwable {
		// Delegate to the JSON value template service.
		final Object jsonValue = JSONValueVelocityTemplateService.getInstance().proxyOutput(t);
		
		// Return the proxied output; serialized as a JSON string. 
		return JSONStringUtils.toJSONString(jsonValue, false);
	}
	
}
