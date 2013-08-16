package uk.ac.soton.mib104.t2.workbench.ui.json.text;

import java.util.Set;

import net.sf.taverna.t2.lang.ui.EditorKeySetUtil;

import uk.ac.soton.mib104.t2.activities.json.JSONStringUtils;
import uk.ac.soton.mib104.t2.workbench.ui.KeywordDocumentEditorPane;

/**
 * Keyword document editor pane that is specialized for JSON documents. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONKeywordDocumentEditorPane extends KeywordDocumentEditorPane {

	private static final Set<String> KEYS = EditorKeySetUtil.loadKeySet(JSONKeywordDocumentEditorPane.class.getResourceAsStream("/keys.txt"));

	private static final long serialVersionUID = -3548851147277121496L;

	/**
	 * Empty constructor.
	 * <p>
	 * Equivalent to: <code>JSONKeywordDocumentEditorPane(null);</code>
	 */
	public JSONKeywordDocumentEditorPane() {
		this(null);
	}

	/**
	 * Default constructor.
	 * 
	 * @param t  the initial content of the keyword document
	 */
	public JSONKeywordDocumentEditorPane(final String t) {
		super(t);
	}

	@Override
	protected void doValidate() throws Throwable {
		final String t = this.getText();
		
		@SuppressWarnings("unused")
		final Object jsonValue = JSONStringUtils.parseJSONString(t);
	}

	@Override
	protected String getFileExtension() {
		return "json";
	}

	@Override
	protected Set<String> getKeys() {
		return KEYS;
	}

	@Override
	protected String getKeywordDocumentEditorPaneName() {
		return "JSON document";
	}

}
