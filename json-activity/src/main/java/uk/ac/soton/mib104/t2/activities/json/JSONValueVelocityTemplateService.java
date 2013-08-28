/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

/**
 * Apache Velocity-based template service that returns JSON values. 
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 */
public final class JSONValueVelocityTemplateService extends AbstractVelocityTemplateService<Object> {

	/**
	 * Proxy for lists.
	 * 
	 * @author Mark Borkum
	 * @version 0.0.1-SNAPSHOT
	 */
	private static final class ProxyList extends ProxyObject implements List<ProxyObject> {
		
		private final List<ProxyObject> list;
		
		/**
		 * Sole constructor.
		 * 
		 * @param c
		 */
		public ProxyList(final List<?> c) {
			super(c);
			
			list = createProxyObjectList(c);
		}

		@Override
		public void add(final int index, final ProxyObject element) {
			list.add(index, element);
		}

		@Override
		public boolean add(final ProxyObject e) {
			return list.add(e);
		}

		@Override
		public boolean addAll(final Collection<? extends ProxyObject> c) {
			return list.addAll(c);
		}

		@Override
		public boolean addAll(final int index, final Collection<? extends ProxyObject> c) {
			return list.addAll(index, c);
		}

		@Override
		public void clear() {
			list.clear();
		}

		@Override
		public boolean contains(final Object o) {
			return list.contains(createProxyObject(o));
		}

		@Override
		public boolean containsAll(final Collection<?> c) {
			return list.containsAll(createProxyObjectList(c));
		}

		@Override
		public ProxyObject get(final int index) {
			return list.get(index);
		}

		@Override
		public int indexOf(final Object o) {
			return list.indexOf(createProxyObject(o));
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}

		@Override
		public Iterator<ProxyObject> iterator() {
			return list.iterator();
		}

		@Override
		public int lastIndexOf(final Object o) {
			return list.lastIndexOf(createProxyObject(o));
		}

		@Override
		public ListIterator<ProxyObject> listIterator() {
			return list.listIterator();
		}

		@Override
		public ListIterator<ProxyObject> listIterator(final int index) {
			return list.listIterator(index);
		}

		@Override
		public ProxyObject remove(final int index) {
			return list.remove(index);
		}

		@Override
		public boolean remove(final Object o) {
			return list.remove(createProxyObject(o));
		}

		@Override
		public boolean removeAll(final Collection<?> c) {
			return list.removeAll(createProxyObjectList(c));
		}

		@Override
		public boolean retainAll(final Collection<?> c) {
			return list.retainAll(createProxyObjectList(c));
		}

		@Override
		public ProxyObject set(final int index, final ProxyObject element) {
			return list.set(index, element);
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public List<ProxyObject> subList(final int fromIndex, final int toIndex) {
			return list.subList(fromIndex, toIndex);
		}

		@Override
		public Object[] toArray() {
			return list.toArray();
		}

		@Override
		public <T> T[] toArray(final T[] a) {
			return list.toArray(a);
		}
		
	}
	
	/**
	 * Proxy for maps.
	 * 
	 * @author Mark Borkum
	 * @version 0.0.1-SNAPSHOT
	 */
	private static final class ProxyMap extends ProxyObject implements Map<ProxyObject, ProxyObject> {
		
		private final Map<ProxyObject, ProxyObject> map; 
		
		/**
		 * Sole constructor.
		 * 
		 * @param m
		 */
		public ProxyMap(final Map<?, ?> m) {
			super(m);
			
			map = createProxyObjectMap(m);
		}

		@Override
		public void clear() {
			map.clear();
		}

		@Override
		public boolean containsKey(final Object key) {
			return map.containsKey(createProxyObject(key));
		}

		@Override
		public boolean containsValue(final Object value) {
			return map.containsValue(createProxyObject(value));
		}

		@Override
		public Set<Map.Entry<ProxyObject, ProxyObject>> entrySet() {
			return map.entrySet();
		}

		@Override
		public ProxyObject get(final Object key) {
			return map.get(createProxyObject(key));
		}

		@Override
		public boolean isEmpty() {
			return map.isEmpty();
		}

		@Override
		public Set<ProxyObject> keySet() {
			return map.keySet();
		}

		@Override
		public ProxyObject put(final ProxyObject key, final ProxyObject value) {
			return map.put(key, value);
		}

		@Override
		public void putAll(final Map<? extends ProxyObject, ? extends ProxyObject> m) {
			map.putAll(m);
		}

		@Override
		public ProxyObject remove(final Object key) {
			return map.remove(createProxyObject(key));
		}

		@Override
		public int size() {
			return map.size();
		}

		@Override
		public Collection<ProxyObject> values() {
			return map.values();
		}
		
	}

	/**
	 * Proxy for objects.
	 * 
	 * @author Mark Borkum
	 * @version 0.0.1-SNAPSHOT
	 */
	private static class ProxyObject {
		
		private final Object obj;
		
		/**
		 * Sole constructor.
		 * 
		 * @param obj
		 */
		public ProxyObject(final Object obj) {
			super();
			
			this.obj = obj;
		}
		
		@Override
		public final boolean equals(final Object other) {
			if (other == null) {
				return false;
			} else if (other == this) {
				return true;	
			} else if (other instanceof ProxyObject) {
				return ObjectUtils.equals(obj, ((ProxyObject) other).obj);
			} else {
				return false;
			}
		}

		@Override
		public final int hashCode() {
			return ObjectUtils.hashCode(obj);
		}

		@Override
		public final String toString() {
			return JSONStringUtils.toJSONString(obj, false);
		}
		
	}
	
	private static final class SingletonWrapper {
		
		private static AbstractVelocityTemplateService<Object> INSTANCE;
		
		public static final AbstractVelocityTemplateService<Object> getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new JSONValueVelocityTemplateService();
			}
			
			return INSTANCE;
		}
		
	}
	
	/**
	 * Singleton proxy object for <code>false</code>.
	 * 
	 * @see Boolean.FALSE
	 */
	private static final ProxyObject FALSE = new ProxyObject(false);
	
	/**
	 * Singleton proxy object for <code>null</code>.
	 * 
	 * @see Void
	 */
	private static final ProxyObject NULL = new ProxyObject(null);
	
	/**
	 * Singleton proxy object for <code>true</code>.
	 * 
	 * @see Boolean.TRUE
	 */
	private static final ProxyObject TRUE = new ProxyObject(true);
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	private static final ProxyObject createProxyObject(final Object obj) {
		if (obj == null) {
			return NULL;
		} else if (obj instanceof ProxyObject) {
			return (ProxyObject) obj;
		} else if (Boolean.FALSE.equals(obj)) {
			return FALSE;
		} else if (Boolean.TRUE.equals(obj)) {
			return TRUE;
		} else if (obj instanceof List) {
			return new ProxyList((List<?>) obj);
		} else if (obj instanceof Map) {
			return new ProxyMap((Map<?, ?>) obj);
		} else {
			return new ProxyObject(obj);
		}
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private static final List<ProxyObject> createProxyObjectList(final Collection<?> c) {
		if ((c == null) || c.isEmpty()) {
			return Collections.emptyList();
		} else {
			final List<ProxyObject> list = new ArrayList<ProxyObject>(c.size());
			
			for (final Object e : c) {
				list.add(createProxyObject(e));
			}
			
			return Collections.unmodifiableList(list);
		}
	}
	
	/**
	 * 
	 * @param m
	 * @return
	 */
	private static final Map<ProxyObject, ProxyObject> createProxyObjectMap(final Map<?, ?> m) {
		if ((m == null) || m.isEmpty()) {
			return Collections.emptyMap();
		} else {
			final Map<ProxyObject, ProxyObject> map = new HashMap<ProxyObject, ProxyObject>(m.size());
			
			for (final Map.Entry<?, ?> entry : m.entrySet()) {
				if (entry != null) {
					map.put(createProxyObject(entry.getKey()), createProxyObject(entry.getValue()));
				}
			}
			
			return Collections.unmodifiableMap(map);
		}
	}
	
	public static final AbstractVelocityTemplateService<Object> getInstance() {
		return SingletonWrapper.getInstance();
	}
	
	private JSONValueVelocityTemplateService() {
		super();
	}
	
	@Override
	protected Object proxyInput(final Object obj) throws Throwable {
		// Every object is proxied by an instance of the ProxyObject class, which ensures
		// that 'obj' is immutable, correctly implements equals and hashCode, etc.
		return createProxyObject(obj);
	}

	@Override
	protected Object proxyOutput(final String t) throws Throwable {
		// Parse the result of merging the Apache Velocity template. 
		return JSONStringUtils.parseJSONString(t);
	}

}
