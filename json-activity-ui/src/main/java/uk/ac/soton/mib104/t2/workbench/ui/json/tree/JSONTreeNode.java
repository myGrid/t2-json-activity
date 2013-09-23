/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.workbench.ui.json.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import uk.ac.soton.mib104.t2.activities.json.JSONPathUtils;
import uk.ac.soton.mib104.t2.activities.json.JSONStringUtils;
import uk.ac.soton.mib104.t2.workbench.ui.StringUtils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.JsonProvider;

/**
 * An object that can be used as a node in a {@link JSONTree}.
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 *
 * @param <K>  the type of JSON keys
 * @param <V>  the type of JSON values
 */
public abstract class JSONTreeNode<K, V> implements TreeNode {

	/**
	 * An object that can be used as a node in a {@link JSONTree}, where said node corresponds to a JSON array (or list.)
	 * 
	 * @author Mark Borkum
	 * @version 0.0.1-SNAPSHOT
	 *
	 * @param <K>  the type of JSON keys
	 */
	private static final class JSONTreeNode_List<K> extends JSONTreeNode<K, List<?>> {

		/**
		 * Default constructor.
		 * 
		 * @param parent  the parent tree node
		 * @param key  the JSON key
		 * @param value  the list of JSON values
		 */
		public JSONTreeNode_List(final TreeNode parent, final K key, final List<?> value) {
			super(parent, key, value);
		}

		@Override
		protected Vector<TreeNode> createChildren() {
			final List<?> value = this.getValue();
			
			if ((value == null) || value.isEmpty()) {
				return null;
			} else {
				final List<TreeNode> result = new ArrayList<TreeNode>(value.size());
				
				for (int index = 0, length = value.size(); index < length; index++) {
					Object childValue;
					
					try {
						childValue = value.get(index);
					} catch (final ArrayIndexOutOfBoundsException ex) {
						childValue = null;
					}
					
					result.add(createTreeNode(this, Integer.valueOf(index), childValue));
				}

				return new Vector<TreeNode>(result);
			}
		}
		
	}
	
	/**
	 * An object that can be used as a node in a {@link JSONTree}, where said node corresponds to a JSON object (or map.)
	 * 
	 * @author Mark Borkum
	 * @version 0.0.1-SNAPSHOT
	 *
	 * @param <K>  the type of JSON keys
	 */
	private static final class JSONTreeNode_Map<K> extends JSONTreeNode<K, Map<?, ?>> {

		/**
		 * Default constructor.
		 * 
		 * @param parent  the parent tree node
		 * @param key  the JSON key
		 * @param value  the map of JSON values
		 */
		public JSONTreeNode_Map(final TreeNode parent, final K key, final Map<?, ?> value) {
			super(parent, key, value);
		}

		@Override
		protected Vector<TreeNode> createChildren() {
			final Map<?, ?> value = this.getValue();
			
			if ((value == null) || value.isEmpty()) {
				return null;
			} else {
				final Map<String, Object> map = new TreeMap<String, Object>();
				
				for (final Map.Entry<?, ?> entry : value.entrySet()) {
					map.put(entry.getKey().toString(), entry.getValue());
				}
				
				final List<TreeNode> result = new ArrayList<TreeNode>(map.size());
				
				for (final Map.Entry<String, Object> entry : map.entrySet()) {
					result.add(createTreeNode(this, entry.getKey(), entry.getValue()));
				}
				
				return new Vector<TreeNode>(result);
			}
		}
		
	}
	
	/**
	 * An object that can be used as a node in a {@link JSONTree}, where said node corresponds to any JSON value (or object.) 
	 * 
	 * @author Mark Borkum
	 * @version 0.0.1-SNAPSHOT
	 *
	 * @param <K>  the type of JSON keys
	 * @param <V>  the type of JSON values
	 */
	private static final class JSONTreeNode_Object<K, V> extends JSONTreeNode<K, V> {
		
		/**
		 * Default constructor.
		 * 
		 * @param parent  the parent tree node
		 * @param key  the JSON key
		 * @param value  the JSON value
		 */
		public JSONTreeNode_Object(final TreeNode parent, final K key, final V value) {
			super(parent, key, value);
		}

		@Override
		protected Vector<TreeNode> createChildren() {
			return null;
		}
		
	}
	
	private static final String arrayIndexTokenFormat1 = "[%d]";

	private static final String bracketNotationFieldFormat1 = "['%s']";
	
	private static final String convertToTextDefaultFormat1 = "<font face=\"Monospaced\">%s</font>";
	
	private static final String convertToTextEmpty = "empty";

	private static final String convertToTextFormat1 = "<html>%s</html>";
	
	private static final String convertToTextKeywordFormat1 = "<font face=\"Monospaced\" color=\"blue\"><b>%s</b></font>";
	
	private static final String convertToTextKeywordFormatSelected1 = "<font face=\"Monospaced\" color=\"white\"><b>%s</b></font>";

	private static final String convertToTextListFormat1 = "[&nbsp;<font color=\"gray\">%s</font>&nbsp;]";
	
	private static final String convertToTextListFormatSelected1 = "[&nbsp;<font color=\"white\">%s</font>&nbsp;]";
	
	private static final String convertToTextListPluralFormat1 = "%d elements";
	
	private static final String convertToTextListSingularFormat1 = "%d element";
	
	private static final String convertToTextMapFormat1 = "{&nbsp;<font color=\"gray\">%s</font>&nbsp;}";
	
	private static final String convertToTextMapFormatSelected1 = "{&nbsp;<font color=\"white\">%s</font>&nbsp;}";
	
	private static final String convertToTextMapPluralFormat1 = "%d entries";
	
	private static final String convertToTextMapSingularFormat1 = "%d entry";
	
	private static final String convertToTextNonEmpty = "&hellip;";
	
	private static final String convertToTextStringFormat1 = "<font face=\"Monospaced\" color=\"red\">%s</font>";
	
	private static final String convertToTextStringFormatSelected1 = "<font face=\"Monospaced\" color=\"white\">%s</font>";
	
	private static JsonProvider defaultProvider;
	
	private static final String dotNotationFieldFormat1 = ".%s";
	
	private static final Vector<TreeNode> EMPTY_VECTOR = new Vector<TreeNode>(0);
	
	private static final String keyValuePairFormat2 = "%s&nbsp;<font color=\"gray\">&rarr;</font>&nbsp;%s";
	
	private static final String keyValuePairFormatSelected2 = "%s&nbsp;<font color=\"white\">&rarr;</font>&nbsp;%s";
	
	private static final String rootPathToken = "$";
	
	private static final String shouldUseDotNotationRegex = "^.*?\\W+.*?$";

	/**
	 * Compiles a tree <code>path</code> into a JSONPath expression, or returns <code>null</code>. 
	 * 
	 * @param path  the tree path
	 * @return  the JSONPath expression
	 */
	public static final JsonPath compile(final TreePath path) {
		if (path == null) {
			return null;
		} else {
			// Determine if dot notation should be used (instead of square-bracket notation.)
			final boolean dotNotation = shouldUseDotNotation(path);
			
			final StringBuffer sb = new StringBuffer();
			
			for (int index = 0, length = path.getPathCount(); index < length; index++) {
				final Object obj = path.getPathComponent(index);
				
				if (obj == null) {
					return null;
				} else if (obj instanceof JSONTreeNode) {
					final JSONTreeNode<?, ?> node = (JSONTreeNode<?, ?>) obj;
					
					final Object key = node.getKey();
					
					if (index == 0) {
						if (key == null) {
							sb.append(rootPathToken);
						} else {
							// If the first tree node in the given 'path' has a non-null key, we have an error.  
							return null;
						}
					} else {
						final String compiledKey = compileKey(dotNotation, key);
						
						if (compiledKey == null) {
							// If the next tree node in the given 'path' has a null key, we have an error. 
							return null;
						} else {
							sb.append(compiledKey);
						}
					}
				} else {
					return null;
				}
			}
			
			// Compile the contents of the string buffer. 
			return JsonPath.compile(sb.toString());
		}
	}
	
	/**
	 * Compiles a single key to a component of a JSONPath expression. 
	 * 
	 * @param dotNotation  Specifies if the key should be prefixed with a dot, or wrapped in square brackets. 
	 * @param key  the JSON key
	 * @return  a component of a JSONPath expression
	 */
	private static final String compileKey(final boolean dotNotation, final Object key) {
		if (key == null) {
			return null;
		} else if (key instanceof Integer) {
			return String.format(arrayIndexTokenFormat1, key);
		} else if (key instanceof String) {
			if (dotNotation) {
				return String.format(dotNotationFieldFormat1, key);
			} else {
				return String.format(bracketNotationFieldFormat1, key.toString());
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Called by the renderers to convert the given <code>jsonValue</code> to text. 
	 * 
	 * @param jsonValue  the JSON value to convert to text
	 * @param selected  <code>true</code> if the node is selected
	 * @param expanded  <code>true</code> if the node is expanded
	 * @param leaf  <code>true</code> if the node is a leaf
	 * @param row  an integer specifying the node's display row, where 0 is the first row in the display
	 * @param hasFocus  <code>true</code> if the node has the focus
	 * @return  the <code>String</code> representation of the node's value
	 */
	private static final String convertToText(final Object jsonValue, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
		if (jsonValue == null) {
			return String.format(selected ? convertToTextKeywordFormatSelected1 : convertToTextKeywordFormat1, JSONStringUtils.toJSONString(jsonValue, false));
		} else if (jsonValue instanceof Boolean) {
			return String.format(selected ? convertToTextKeywordFormatSelected1 : convertToTextKeywordFormat1, JSONStringUtils.toJSONString(jsonValue, false));
		} else if (jsonValue instanceof String) {
			return String.format(selected ? convertToTextStringFormatSelected1 : convertToTextStringFormat1, JSONStringUtils.toJSONString(jsonValue, false));
		} else if (jsonValue instanceof List) {
			return String.format(selected ? convertToTextListFormatSelected1 : convertToTextListFormat1, leaf ? convertToTextEmpty : expanded ? convertToTextNonEmpty : StringUtils.pluralize(((List<?>) jsonValue).size(), convertToTextListSingularFormat1, convertToTextListPluralFormat1));
		} else if (jsonValue instanceof Map) {
			return String.format(selected ? convertToTextMapFormatSelected1 : convertToTextMapFormat1, leaf ? convertToTextEmpty : expanded ? convertToTextNonEmpty : StringUtils.pluralize(((Map<?, ?>) jsonValue).size(), convertToTextMapSingularFormat1, convertToTextMapPluralFormat1));
		} else {
			return String.format(convertToTextDefaultFormat1, JSONStringUtils.toJSONString(jsonValue, false));
		}
	}
	
	/**
	 * Returns the default JSON provider.
	 * 
	 * @return  the default JSON provider
	 */
	public static final JsonProvider createProvider() {
		if (defaultProvider == null) {
			defaultProvider = new JSONTreeNodeJsonProvider(JSONPathUtils.createProvider());
		}
		
		return defaultProvider;
	}
	
	/**
	 * Constructs a new tree node for the given <code>jsonValue</code>.
	 * 
	 * @param jsonValue  the JSON value
	 * @return  a new tree node
	 */
	public static final TreeNode createTreeNode(final Object jsonValue) {
		return createTreeNode(null, null, jsonValue);
	}
	
	/**
	 * Constructs a new tree node for the given <code>parent</code>, <code>key</code> and <code>value</code>.
	 * 
	 * @param <K>  the type of JSON keys
	 * @param <V>  the type of JSON values
	 * @param parent  the parent tree node
	 * @param key  the JSON key
	 * @param value  the JSON value
	 * @return  a new tree node
	 */
	private static final <K, V> TreeNode createTreeNode(final TreeNode parent, final K key, final V value) {
		if (value == null) {
			return new JSONTreeNode_Object<K, V>(parent, key, value);
		} else if (value instanceof List) {
			return new JSONTreeNode_List<K>(parent, key, (List<?>) value);
		} else if (value instanceof Map) {
			return new JSONTreeNode_Map<K>(parent, key, (Map<?, ?>) value);
		} else {
			return new JSONTreeNode_Object<K, V>(parent, key, value);
		}
	}
	
	/**
	 * Returns <code>true</code> if dot notation should be used to compile the given tree <code>path</code>.
	 * 
	 * @param path  the tree path
	 * @return  <code>true</code> if dot notation should be used, otherwise <code>false</code>
	 */
	private static final boolean shouldUseDotNotation(final TreePath path) {
		if (path != null) {
			for (int index = 0, length = path.getPathCount(); index < length; index++) {
				final Object obj = path.getPathComponent(index);
				
				if (obj instanceof JSONTreeNode) {
					final Object key = ((JSONTreeNode<?, ?>) obj).getKey();
					
					if ((key != null) && (key instanceof String) && key.toString().matches(shouldUseDotNotationRegex)) {
						// If the given 'key' is a string, but does not match the specified regular expression, then return false. 
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private final Vector<TreeNode> children;

	private final K key;

	private final TreeNode parent;
	
	private final V value;

	/**
	 * Default constructor.
	 * 
	 * @param parent  the parent tree node
	 * @param key  the JSON key
	 * @param value  the JSON value
	 */
	protected JSONTreeNode(final TreeNode parent, final K key, final V value) {
		super();
		
		this.parent = parent;
		this.key = key;
		this.value = value;
		
		// Create the list of children *after* other attributes have been set. 
		this.children = this.createChildren();
	}

	@Override
	public final Enumeration<TreeNode> children() {
		return this.getChildren().elements();
	}
	
	/**
	 * Called by the renderers to convert this node to text. 
	 * 
	 * @param selected  <code>true</code> if this node is selected
	 * @param expanded  <code>true</code> if this node is expanded
	 * @param leaf  <code>true</code> if this node is a leaf
	 * @param row  an integer specifying this node's display row, where 0 is the first row in the display
	 * @param hasFocus  <code>true</code> if this node has the focus
	 * @return  the <code>String</code> representation of this node's value
	 */
	public final String convertToText(final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
		// If the "key" is an array or object, then ensure that the textual
		// representation is *not* expanded (regardless of the value of the
		// "expanded" argument.) 
		return String.format(convertToTextFormat1, (parent == null) ? convertToText(value, selected, expanded, leaf, row, hasFocus) : String.format(selected ? keyValuePairFormatSelected2 : keyValuePairFormat2, convertToText(key, selected, false, leaf, row, hasFocus), convertToText(value, selected, expanded, leaf, row, hasFocus)));
	}

	/**
	 * Called by constructor to create list of child nodes.
	 * <p>
	 * If this node does not allow child nodes, then this method should return <code>null</code>. 
	 * 
	 * @return  list of child nodes
	 */
	protected abstract Vector<TreeNode> createChildren();
	
	@Override
	public final boolean getAllowsChildren() {
		return (children != null);
	}
	
	@Override
	public final TreeNode getChildAt(final int childIndex) throws ArrayIndexOutOfBoundsException {
		return this.getChildren().elementAt(childIndex);
	}
	
	@Override
	public final int getChildCount() {
		return this.getChildren().size();
	}
	
	/**
	 * Returns the list of child nodes. 
	 * 
	 * @return  the list of child nodes
	 */
	private Vector<TreeNode> getChildren() {
		if (children == null) {
			// If the vector of "children" is null, then always return the same
			// empty vector. The main advantage of this approach is that it
			// reduces the memory overhead when loading large trees. 
			return EMPTY_VECTOR;
		} else {
			return children;
		}
	}
	
	@Override
	public final int getIndex(final TreeNode node) {
		return this.getChildren().indexOf(node);
	}
	
	/**
	 * Returns the JSON key for this node. 
	 * 
	 * @return  the JSON key
	 */
	public final K getKey() {
		return key;
	}
	
	@Override
	public final TreeNode getParent() {
		return parent;
	}
	
	/**
	 * Returns the JSON value for this node.
	 * 
	 * @return  the JSON value
	 */
	public final V getValue() {
		return value;
	}
	
	@Override
	public final boolean isLeaf() {
		return this.getChildren().isEmpty();
	}
	
	/**
	 * Applies the given <code>jsonPath</code> to this node. 
	 * 
	 * @param <T>  expected return type
	 * @param jsonPath  the JSONPath expression
	 * @return  the result of the application
	 */
	public final <T> T read(final JsonPath jsonPath) {
		return JSONPathUtils.read(createProvider(), jsonPath, this);
	}
	
}
