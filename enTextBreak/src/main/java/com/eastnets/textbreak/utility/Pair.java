package com.eastnets.textbreak.utility;

import java.io.Serializable;

/**
 * Pair POJO
 * 
 * @author EastNets
 * @since 2022
 */
public class Pair<K, V> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5010921395904660247L;
	private K key;
	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public Pair() {
		this.key = null;
		this.value = null;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return key.hashCode() ^ value.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Pair))
			return false;
		Pair<?, ?> pairo = (Pair<?, ?>) o;
		return this.key.equals(pairo.getKey()) && this.value.equals(pairo.getValue());
	}

}