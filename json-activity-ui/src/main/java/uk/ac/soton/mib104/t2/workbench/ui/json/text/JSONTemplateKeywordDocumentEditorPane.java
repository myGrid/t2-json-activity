/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.workbench.ui.json.text;

import java.util.Collections;
import java.util.Set;

import javax.swing.Icon;

import net.sf.taverna.t2.lang.ui.EditorKeySetUtil;
import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workbench.icons.WorkbenchIcons;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;

import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivity;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.json.JSONTemplateActivityHealthChecker;
import uk.ac.soton.mib104.t2.activities.json.ui.config.JSONTemplateConfigurationPanel;
import uk.ac.soton.mib104.t2.workbench.ui.Cardinality;
import uk.ac.soton.mib104.t2.workbench.ui.KeywordDocumentEditorPane;

/**
 * Keyword document editor pane that is specialized for Apache Velocity templates that render JSON documents (referred to as "JSON templates".) 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONTemplateKeywordDocumentEditorPane extends KeywordDocumentEditorPane {
	
	private static final Set<String> KEYS = EditorKeySetUtil.loadKeySet(JSONTemplateKeywordDocumentEditorPane.class.getResourceAsStream("/keys.txt"));

	private static final long serialVersionUID = -2828622828219150422L;
	
	/**
	 * Reference to the configuration panel that contains this editor pane.
	 * <p>
	 * This reference is necessary so that "clear text" can be automatically generated
	 * using the current state of the editor pane, e.g., the names of input ports.  
	 */
	private final JSONTemplateConfigurationPanel configPane;
	
	/**
	 * Empty[-ish] constructor.
	 * <p>
	 * Equivalent to: <code>JSONTemplateKeywordDocumentEditorPane(configPane, null);</code>
	 * 
	 * @param configPane  the configuration panel
	 */
	public JSONTemplateKeywordDocumentEditorPane(final JSONTemplateConfigurationPanel configPane) {
		this(configPane, null);
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param configPane  the configuration panel
	 * @param t  the initial content of the keyword document
	 */
	public JSONTemplateKeywordDocumentEditorPane(final JSONTemplateConfigurationPanel configPane, final String t) {
		super(t);
		
		if (configPane == null) {
			throw new IllegalArgumentException(new NullPointerException("configPane"));
		}
		
		this.configPane = configPane;
	}

	@Override
	protected void doValidate() throws Throwable {
		final JSONTemplateActivityHealthChecker healthChecker = new JSONTemplateActivityHealthChecker();
		
		final JSONTemplateActivity mockActivity = new JSONTemplateActivity();
		
		final JSONTemplateActivityConfigurationBean configBean = configPane.toConfiguration(Cardinality.MANY, Cardinality.ONE);
		
		mockActivity.configure(configBean);
		
		final VisitReport report = healthChecker.visit(mockActivity, Collections.emptyList());

		for (final VisitReport subReport : report.getSubReports()) {
			if ((HealthCheck.INVALID_SCRIPT == subReport.getResultId()) && (Status.SEVERE == subReport.getStatus())) {
				throw new IllegalArgumentException(subReport.getMessage());
			}
		}
	}

	@Override
	protected Icon getClearButtonIcon() {
		return WorkbenchIcons.refreshIcon;
	}

	@Override
	protected String getClearButtonText() {
		return "Refresh template";
	}

	@Override
	protected String getClearButtonToolTipText() {
		return String.format("Refresh %s (replace with default: an object, with one key-value pair for each input port)", this.getKeywordDocumentEditorPaneName());
	}

	@Override
	protected String getClearText() {
		return configPane.defaultTemplate();
	}

	@Override
	protected String getFileExtension() {
		return "jsont";
	}

	@Override
	protected Set<String> getKeys() {
		return KEYS;
	}

	@Override
	protected String getKeywordDocumentEditorPaneName() {
		return "JSON template";
	}

	@Override
	protected String getOpenButtonText() {
		return "Open template";
	}

	@Override
	protected String getSaveButtonText() {
		return "Save template";
	}

	@Override
	protected String getValidateButtonText() {
		return "Validate template";
	}

	@Override
	protected String getValidateButtonToolTipText() {
		return String.format("Validate %s using service health checker", this.getKeywordDocumentEditorPaneName());
	}

	@Override
	protected boolean isClearButtonEnabled() {
		return true;
	}

}
