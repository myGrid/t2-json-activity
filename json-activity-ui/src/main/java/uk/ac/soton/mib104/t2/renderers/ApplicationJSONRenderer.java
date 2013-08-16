package uk.ac.soton.mib104.t2.renderers;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.reference.T2ReferenceType;
import net.sf.taverna.t2.renderers.Renderer;
import net.sf.taverna.t2.renderers.RendererException;
import net.sf.taverna.t2.workbench.icons.WorkbenchIcons;

import uk.ac.soton.mib104.t2.activities.json.JSONStringUtils;
import uk.ac.soton.mib104.t2.workbench.ui.TreeUtils;
import uk.ac.soton.mib104.t2.workbench.ui.json.tree.JSONTree;

/**
 * Renderer for "application/json" MIME type.
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class ApplicationJSONRenderer implements Renderer {
	
	/**
	 * Number of Bytes (B) per MegaByte (MiB)
	 */
	private static final long bytesPerMegaByte = 1024 * 1024;
	
	/**
	 * Renders the given <code>reference</code> using the <code>referenceService</code> and returns the file size in Bytes. 
	 * 
	 * @param referenceService  the reference service
	 * @param reference  the reference
	 * @return  the file size in bytes
	 * @throws RendererException  
	 */
	private static final long getSizeBytes(final ReferenceService referenceService, final T2Reference reference) throws RendererException {
		final T2ReferenceType referenceType = reference.getReferenceType();
		
		switch (referenceType) {
		case ReferenceSet:
			try {
				return referenceService.getReferenceSetService().getReferenceSet(reference).getApproximateSizeInBytes().longValue();
			} catch (final Throwable ex) {
				throw new RendererException("JSON Renderer: Unable to determine size of referenced data", ex);
			}
		default:
			throw new RendererException(String.format("JSON Renderer: Invalid reference type. Expected: %s. Received: %s", T2ReferenceType.ReferenceSet, referenceType));
		}
	}
	
	/**
	 * Renders the given <code>reference</code> using the <code>referenceService</code> and returns the contents of the file as a JSON object. 
	 * 
	 * @param referenceService  the reference service
	 * @param reference  the reference
	 * @return  the contents of the file as a JSON object
	 * @throws RendererException
	 */
	private static final Object renderAsJSON(final ReferenceService referenceService, final T2Reference reference) throws RendererException {
		// Calculate the size in bytes.
		final long sizeBytes = getSizeBytes(referenceService, reference);
		
		if (sizeBytes >= bytesPerMegaByte) {
			// If the calculated size in Bytes is greater than 1 MiB, then show the
			// confirmation dialog.  
			switch (JOptionPane.showConfirmDialog(null, String.format("Result size in bytes is approximately %s MiB. When rendered, this may cause issues in Taverna Workbench - do you want to continue?", toMegaBytes(sizeBytes), System.getProperty("line.separator")), "Render result as JSON?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, WorkbenchIcons.questionMessageIcon)) {
			case JOptionPane.YES_OPTION:
				break;
			default:
				throw new RendererException(String.format("JSON Renderer: Cancelled (data exceeded %s MiB)", toMegaBytes(bytesPerMegaByte)));
			}
		}
		
		String jsonString;
		
		try {
			// Render the given 'reference' as a String.
			jsonString = (String) referenceService.renderIdentifier(reference, String.class, null);
		} catch (final Throwable ex) {
			throw new RendererException("JSON Renderer: Failed to render reference", ex);
		}
		
		Object jsonValue;
		
		try {
			// Parse the String to a JSON value. 
			jsonValue = JSONStringUtils.parseJSONString(jsonString);
		} catch (final Throwable ex) {
			throw new RendererException("JSON Renderer: Failed to parse data", ex);
		}
		
		// Success!!
		return jsonValue;
	}
	
	/**
	 * Converts the given value from Bytes (B) to MegaBytes (MiB). 
	 * 
	 * @param n  the number of Bytes (B)
	 * @return  the number of MegaBytes (MiB)
	 */
	private static final double toMegaBytes(final long n) {
		return Math.round(Long.valueOf(n).doubleValue() / bytesPerMegaByte);
	}

	@Override
	public boolean canHandle(final ReferenceService referenceService, final T2Reference reference, final String mimeType) throws RendererException {
//		if (this.canHandle(mimeType)) {
//			return true;
//		} else {
//			try {
//				final long sizeBytes = getSizeBytes(referenceService, reference);
//				
//				if (sizeBytes >= bytesPerMegaByte) {
//					return false;
//				} else {
//					@SuppressWarnings("unused")
//					final Object jsonValue = renderAsJSON(referenceService, reference);
//					
//					return true;
//				}
//			} catch (final Throwable ex) {
//				return false;
//			}
//		}
		return this.canHandle(mimeType);
	}
	
	@Override
	public boolean canHandle(final String mimeType) {
		if (mimeType == null) {
			return false;
		} else {
			return mimeType.trim().equalsIgnoreCase("application/json");
		}
	}

	@Override
	public JComponent getComponent(final ReferenceService referenceService, final T2Reference reference) throws RendererException {
		try {
			// Render the given 'reference' as a JSON value.
			final Object jsonValue = renderAsJSON(referenceService, reference);
			
			// Construct a JSON tree.
			final JTree tree = new JSONTree(jsonValue);
			
			// Ensure that no tree nodes can be selected.
			tree.setSelectionModel(JSONTree.emptySelectionModel());
			
			// Expand the entire tree.
			TreeUtils.expandOrCollapseAllRows(tree, true);
			
			// Success!!
			return tree;
		} catch (final RendererException ex) {
			final StringWriter sw = new StringWriter();
			
			ex.printStackTrace(new PrintWriter(sw));
			
			final JTextArea textArea = new JTextArea();
			textArea.setFont(textArea.getFont().deriveFont(11f));
			textArea.setText(sw.toString());
			textArea.setEditable(false);
			return textArea;
		}
	}
	
	@Override
	public String getType() {
		return "JSON";
	}

}
