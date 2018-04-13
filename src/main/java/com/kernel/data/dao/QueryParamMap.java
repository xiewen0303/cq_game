package com.kernel.data.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description 数据库查询参数集合
 * @author ShiJie Chi
 * @created 2010-4-9 下午03:43:50	
 */
public class QueryParamMap<K, V> implements Map<K, V>,Serializable {

	private Map<K, V> map = new HashMap<K, V>();
	
	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object property) {
		return map.containsKey(property);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public V get(Object property) {
		return map.get(property);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public V put(K property, V value) {
		return map.put(property, value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	public V remove(Object property) {
		return map.remove(property);
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		return map.values();
	}

}
