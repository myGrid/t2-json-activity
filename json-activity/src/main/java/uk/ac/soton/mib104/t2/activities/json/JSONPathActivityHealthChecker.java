/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.util.LinkedList;
import java.util.List;

import com.jayway.jsonpath.JsonPath;

import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;

/**
 * Health-checker for instances of {@link JSONPathActivity} class.
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONPathActivityHealthChecker implements HealthChecker<JSONPathActivity> {

	@Override
	public boolean canVisit(final Object object) {
		if (object == null) {
			return false;
		} else {
			return (object instanceof JSONPathActivity);
		}
	}

	@Override
	public boolean isTimeConsuming() {
		return false;
	}

	@Override
	public VisitReport visit(final JSONPathActivity activity, final List<Object> ancestry) {
		if (activity == null) {
			throw new IllegalArgumentException(new NullPointerException("activity"));
		}
		
		final HealthCheck healthCheck = HealthCheck.getInstance();
		
		final List<VisitReport> subReports = new LinkedList<VisitReport>();
		
		final JSONPathActivityConfigurationBean configBean = activity.getConfiguration();
		
		if (configBean == null) {
			subReports.add(new VisitReport(healthCheck, activity, "No configuration", HealthCheck.NO_CONFIGURATION, Status.SEVERE));
		} else {
			if (configBean.getDepth() < 0) {
				subReports.add(new VisitReport(healthCheck, activity, String.format("Invalid depth (too small): %d", configBean.getDepth()), HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
			}
			
			try {
				@SuppressWarnings("unused")
				final JsonPath jsonPath = JsonPath.compile(configBean.getJsonPathString());
				
				// Success!!
			} catch (final Throwable ex) {
				subReports.add(new VisitReport(healthCheck, activity, String.format("Invalid JSONPath: %s", ex.getMessage()), HealthCheck.INVALID_CONFIGURATION, Status.SEVERE));
			}
		}

		return new VisitReport(healthCheck, activity, "JSONPath service is OK", HealthCheck.NO_PROBLEM, subReports);
	}

}
