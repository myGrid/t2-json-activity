/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

/**
 * JSONPath activity.
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONPathActivity extends AbstractAsynchronousActivity<JSONPathActivityConfigurationBean> implements AsynchronousActivity<JSONPathActivityConfigurationBean> {

	private JSONPathActivityConfigurationBean configBean;
	
	@Override
	public void configure(final JSONPathActivityConfigurationBean configBean) throws ActivityConfigurationException {
		this.configBean = configBean;
		
		this.removeInputs();
		this.removeOutputs();
		
		if (configBean != null) {
			this.configurePorts(configBean.toPortsDefinition());
		}
	}
	
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs, final AsynchronousActivityCallback callback) throws IllegalArgumentException {
		if (inputs == null) {
			throw new IllegalArgumentException(new NullPointerException("inputs"));
		} else if (callback == null) {
			throw new IllegalArgumentException(new NullPointerException("callback"));
		}
		
		callback.requestRun(new Runnable() {
			
			@Override
			public void run() {
				final InvocationContext context = callback.getContext();
				
				final ReferenceService referenceService = context.getReferenceService();
				
				final Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				
				final JSONPathActivityConfigurationBean configBean = JSONPathActivity.this.getConfiguration();
				
				if (configBean != null) {
					final String jsonPathString = configBean.getJsonPathString();
					
					final JsonPath jsonPath;
					
					try {
						jsonPath = JsonPath.compile(jsonPathString);
					} catch (final Throwable ex) {
						callback.fail("Failed to compile specified JSONPath", ex);
						
						return;
					}
					
					final Object jsonValue;
					
					try {
						final Object jsonString = referenceService.renderIdentifier(inputs.get(JSONPathActivityConfigurationBean.portNameInput), JSONPathActivityConfigurationBean.translatedElementTypeInput, context);
						
						jsonValue = JSONStringUtils.parseJSONString((jsonString == null) ? null : jsonString.toString());
					} catch (final ReferenceServiceException ex) {
						callback.fail(String.format("Failed to render input: %s", JSONPathActivityConfigurationBean.portNameInput), ex);
						
						return;
					} catch (final Throwable ex) {
						callback.fail(String.format("Unable to process input: %s", JSONPathActivityConfigurationBean.portNameInput), ex);
						
						return;
					}
					
					final int depth = configBean.getDepth();
					
					if (depth > 0) {
						List<Object> resultValues = null;
						
						try {
							resultValues = JSONPathUtils.read(jsonPath, jsonValue);
						} catch (final Throwable ex) {
							// Do nothing...
						}
						
						if (resultValues == null) {
							resultValues = Collections.emptyList();
						}
						
						try {
							final String resultString = JSONStringUtils.toJSONString(resultValues, false);
							
							outputs.put(JSONPathActivityConfigurationBean.portNameOutput, referenceService.register(resultString, JSONPathActivityConfigurationBean.depthOutput, true, context));
						} catch (final ReferenceServiceException ex) {
							callback.fail(String.format("Failed to register output: %s", JSONPathActivityConfigurationBean.portNameOutput), ex);
							
							return;
						} catch (final Throwable ex) {
							callback.fail(String.format("Unable to process output: %s", JSONPathActivityConfigurationBean.portNameOutput), ex);
							
							return;
						}
						
						try {
							final List<Object> resultStrings = JSONStringUtils.toJSONStringList(resultValues, false);
							
							outputs.put(JSONPathActivityConfigurationBean.portNameOutputList, referenceService.register(resultStrings, depth, true, context));
						} catch (final ReferenceServiceException ex) {
							callback.fail(String.format("Failed to register output: %s", JSONPathActivityConfigurationBean.portNameOutputList), ex);
							
							return;
						} catch (final Throwable ex) {
							callback.fail(String.format("Unable to process output: %s", JSONPathActivityConfigurationBean.portNameOutputList), ex);
							
							return;
						}
					} else {
						final Object resultValue;
						
						try {
							resultValue = JSONPathUtils.read(jsonPath, jsonValue);
						} catch (final Throwable ex) {
							callback.fail("Found no matches for specified JSONPath");
							
							return;
						}
						
						if (resultValue == null) {
							// Do nothing...
						} else if (resultValue instanceof List) {
							callback.fail("Port depth mismatch (Expected: 0. Received: >0)");
							
							return;
						}
						
						try {
							final String resultString = JSONStringUtils.toJSONString(resultValue, false);
							
							outputs.put(JSONPathActivityConfigurationBean.portNameOutput, referenceService.register(resultString, JSONPathActivityConfigurationBean.depthOutput, true, context));
						} catch (final ReferenceServiceException ex) {
							callback.fail(String.format("Failed to register output: %s", JSONPathActivityConfigurationBean.portNameOutput), ex);
							
							return;
						} catch (final Throwable ex) {
							callback.fail(String.format("Unable to process output: %s", JSONPathActivityConfigurationBean.portNameOutput), ex);
							
							return;
						}
					}
				}
				
				callback.receiveResult(Collections.unmodifiableMap(outputs), new int[0]);
			}
			
		});
	}
	
	@Override
	public JSONPathActivityConfigurationBean getConfiguration() {
		return configBean;
	}

}
