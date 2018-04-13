package com.junyou.utils.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 只读map
 * @author DaoZheng Yuan
 * 2014-11-12 下午7:06:17
 */
public class ReadOnlyMap<K, V> implements ConcurrentMap<K, V>,Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<K, V> map;
	
	public ReadOnlyMap(Map<K, V> map) {
		this.map = map;
	}
	
	public void clear() {
		throw new UnsupportedOperationException("read only map!");
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
		throw new UnsupportedOperationException("read only map!");
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("read only map!");
	}

	public V remove(Object property) {
		throw new UnsupportedOperationException("read only map!");
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		return map.values();
	}
	
	@Override
	public V putIfAbsent(K key, V value) {
		throw new UnsupportedOperationException("read only map!");
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException("read only map!");
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		throw new UnsupportedOperationException("read only map!");
	}

	@Override
	public V replace(K key, V value) {
		throw new UnsupportedOperationException("read only map!");
	}

}
