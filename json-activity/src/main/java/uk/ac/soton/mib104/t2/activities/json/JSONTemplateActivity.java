package uk.ac.soton.mib104.t2.activities.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

/**
 * JSON template activity. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONTemplateActivity extends AbstractAsynchronousActivity<JSONTemplateActivityConfigurationBean> implements AsynchronousActivity<JSONTemplateActivityConfigurationBean> {
	
	private JSONTemplateActivityConfigurationBean configBean;
	
	@Override
	public void configure(final JSONTemplateActivityConfigurationBean configBean) throws ActivityConfigurationException {
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
				
				final JSONTemplateActivityConfigurationBean configBean = JSONTemplateActivity.this.getConfiguration();
				
				if (configBean != null) {
					final Map<String, Object> bindings = new HashMap<String, Object>();
					
					final Map<String, JSONTemplateActivityInputPortConfigurationBean> inputPortConfigBeans = configBean.getInputPorts();
					
					if (inputPortConfigBeans != null) {
						for (final ActivityInputPort inputPort : JSONTemplateActivity.this.getInputPorts()) {
							final String portName = inputPort.getName();
							
							final JSONTemplateActivityInputPortConfigurationBean inputPortConfigBean = inputPortConfigBeans.get(portName);
							
							if (inputPortConfigBean == null) {
								callback.fail(String.format("Input port configuration not found: %s", portName));
								
								continue;
							}
							
							try {
								final Object obj = referenceService.renderIdentifier(inputs.get(portName), inputPort.getTranslatedElementClass(), context);
								
								final Object jsonValue = inputPortConfigBean.getType().parse(obj);
								
								bindings.put(portName, jsonValue);
							} catch (final Throwable ex) {
								callback.fail(String.format("Failed to render input: %s", portName), ex);
							}
						}
					}
					
					final Map<String, String> outputPortConfigBeans = configBean.getOutputPorts();
					
					if (outputPortConfigBeans != null) {
						for (final OutputPort outputPort : JSONTemplateActivity.this.getOutputPorts()) {
							final String portName = outputPort.getName();
							
							final String t = outputPortConfigBeans.get(portName);
							
							if (t == null) {
								callback.fail(String.format("Output port configuration not found: %s", portName));
								
								continue;
							}
							
							try {
								final String jsonString = JSONStringVelocityTemplateService.getInstance().mergeTemplate(t, bindings);
								
								final T2Reference identifier = referenceService.register(jsonString, outputPort.getDepth(), true, context);
								
								outputs.put(portName, identifier);
							} catch (final Throwable ex) {
								callback.fail(String.format("Failed to register output: %s", portName), ex);
								
								continue;
							}
						}
					}
				}
				
				callback.receiveResult(Collections.unmodifiableMap(outputs), new int[0]);
			}
		});
	}
	
	@Override
	public JSONTemplateActivityConfigurationBean getConfiguration() {
		return configBean;
	}

}
