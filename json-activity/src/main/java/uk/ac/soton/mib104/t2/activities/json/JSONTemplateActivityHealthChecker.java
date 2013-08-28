/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;

import org.apache.velocity.Template;

/**
 * Health-checker for instances of {@link JSONTemplateActivity} class. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONTemplateActivityHealthChecker implements HealthChecker<JSONTemplateActivity> {
	
	@Override
	public boolean canVisit(final Object object) {
		if (object == null) {
			return false;
		} else {
			return (object instanceof JSONTemplateActivity);
		}
	}

	@Override
	public boolean isTimeConsuming() {
		return false;
	}

	@Override
	public VisitReport visit(final JSONTemplateActivity activity, final List<Object> ancestry) throws IllegalArgumentException {
		if (activity == null) {
			throw new IllegalArgumentException(new NullPointerException("activity"));
		}
		
		// Ensure that the same instance is reused for every VisitReport. 
		final HealthCheck healthCheck = HealthCheck.getInstance();
		
		final List<VisitReport> subReports = new LinkedList<VisitReport>();
		
		final JSONTemplateActivityConfigurationBean configBean = activity.getConfiguration();
		
		if (configBean == null) {
			subReports.add(new VisitReport(healthCheck, activity, "Service has no configuration (undefined)", HealthCheck.NO_CONFIGURATION, Status.SEVERE));
		} else {
			final Map<String, JSONTemplateActivityInputPortConfigurationBean> inputPorts = configBean.getInputPorts();
			
			final Map<String, String> outputPorts = configBean.getOutputPorts();
			
			if (((inputPorts == null) || inputPorts.isEmpty()) && ((outputPorts == null) || outputPorts.isEmpty())) {
				subReports.add(new VisitReport(healthCheck, activity, "Service has no input or output ports", HealthCheck.INVALID_SCRIPT, Status.SEVERE));
			} else {
				// Create a map of "empty" bindings (either "null" or the empty list; depending 
				// on the expected granularity of the input port.)
				final Map<String, Object> emptyBindings = new HashMap<String, Object>();
				
				// Create a set of "unused" input port names.  The set is seeded with the names 
				// of all declared input ports.  When an input port is discovered (e.g., if it 
				// is referenced by an output port), the name is removed from this set.  
				final Set<String> unusedInputPortNames = new HashSet<String>();
				
				if (inputPorts == null) {
					subReports.add(new VisitReport(healthCheck, activity, "Service has no input ports (undefined)", HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
				} else if (inputPorts.isEmpty()) {
					subReports.add(new VisitReport(healthCheck, activity, "Service has no input ports", HealthCheck.INVALID_CONFIGURATION, Status.WARNING));
				} else {
					for (final Map.Entry<String, JSONTemplateActivityInputPortConfigurationBean> entry : inputPorts.entrySet()) {
						final String portName = entry.getKey();
						
						if (!VelocityTemplateUtils.isVariableName(portName)) {
							subReports.add(new VisitReport(healthCheck, activity, String.format("Invalid input port name: \"%s\"", portName), HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
						}
						
						final JSONTemplateActivityInputPortConfigurationBean inputPortBean = entry.getValue();
						
						if (inputPortBean.getType() == null) {
							subReports.add(new VisitReport(healthCheck, activity, String.format("Input port \"%s\" does not specify a type", portName), HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
						}
						
						if (0 > inputPortBean.getDepth()) {
							subReports.add(new VisitReport(healthCheck, activity, String.format("Input port \"%s\" specifies a negative depth: %d", portName, inputPortBean.getDepth()), HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
						}
						
						// Add the "empty" binding to the map. 
						emptyBindings.put(portName, (inputPortBean.getDepth() == 0) ? null : Collections.emptyList());
						
						// Add the current port name to the set of "unused" input port names. 
						unusedInputPortNames.add(portName);
					}
				}
				
				if (outputPorts == null) {
					subReports.add(new VisitReport(healthCheck, activity, "Service has no output ports (undefined)", HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
				} else if (outputPorts.isEmpty()) {
					subReports.add(new VisitReport(healthCheck, activity, "Service has no output ports", HealthCheck.INVALID_CONFIGURATION, Status.WARNING));
				} else {
					for (final Map.Entry<String, String> entry : outputPorts.entrySet()) {
						final String portName = entry.getKey();
						
						if (!VelocityTemplateUtils.isVariableName(portName)) {
							subReports.add(new VisitReport(healthCheck, activity, String.format("Invalid output port name: \"%s\"", portName), HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
						}
						
						final String t = entry.getValue();
						
						if (t == null) {
							subReports.add(new VisitReport(healthCheck, activity, String.format("Template \"%s\" is undefined", portName), HealthCheck.INVALID_SCRIPT, Status.SEVERE));
						} else if (t.isEmpty()) {
							subReports.add(new VisitReport(healthCheck, activity, String.format("Template \"%s\" is empty", portName), HealthCheck.INVALID_SCRIPT, Status.SEVERE));
						} else {
							try {
								final Template template = JSONStringVelocityTemplateService.getInstance().createTemplate(t);
								
								final Set<String> directiveVarNames = VelocityTemplateUtils.getVariableNames(template, true);
								
								for (final String otherPortName : directiveVarNames) {
									// Check that new variables (introduced by Apache Velocity #directives) do not have
									// ambiguous names.  
									if (inputPorts.containsKey(otherPortName)) {
										subReports.add(new VisitReport(healthCheck, activity, String.format("Template \"%s\" contains ambiguous directive \"%s\"", portName, otherPortName), HealthCheck.INVALID_SCRIPT, Status.SEVERE));
									}
								}
								
								final Set<String> varNames = VelocityTemplateUtils.getVariableNames(template);
								
								for (final String otherPortName : varNames) {
									// Remove each port name from the list of "unused" input ports.  
									// The Collection#remove method is idempotent.  Hence, an exception will not be thrown
									// if the set does not contain the given port name.  
									unusedInputPortNames.remove(otherPortName);
									
									if (inputPorts.containsKey(otherPortName) || directiveVarNames.contains(otherPortName)) {
										continue;
									} else {
										subReports.add(new VisitReport(healthCheck, activity, String.format("Template \"%s\" contains reference to undefined input port \"%s\"", portName, otherPortName), HealthCheck.INVALID_SCRIPT, Status.SEVERE));
									}
								}
								
								try {
									@SuppressWarnings("unused")
									final Object jsonValue = JSONStringVelocityTemplateService.getInstance().mergeTemplate(template, emptyBindings);
									
									// Success!!
								} catch (final Throwable ex) {
									subReports.add(new VisitReport(healthCheck, activity, String.format("Template \"%s\" contains JSON syntax errors: %s", portName, ex.toString()), HealthCheck.INVALID_SCRIPT, Status.SEVERE));
								}
							} catch (final Throwable ex) {
								subReports.add(new VisitReport(healthCheck, activity, String.format("Template \"%s\" contains Velocity Template Language (VTL) syntax errors: %s", portName, ex.toString()), HealthCheck.INVALID_SCRIPT, Status.SEVERE));
							}
						}
					}
				}
				
				for (final String portName : unusedInputPortNames) {
					// Now that we have processed every input and output port, if this set has any elements, 
					// then we should report an error. 
					subReports.add(new VisitReport(healthCheck, activity, String.format("Input port \"%s\" is never referenced", portName), HealthCheck.INVALID_SCRIPT, Status.WARNING));
				}
			}
		}

		return new VisitReport(healthCheck, activity, "JSON Template service is OK", HealthCheck.NO_PROBLEM, subReports);
	}

}
