/**
 * 
 */
package com.fedfus.common.map;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.collections4.map.AbstractMapDecorator;
import org.apache.commons.collections4.map.UnmodifiableMap;

import lombok.NonNull;

/**
 * @author F.Fusto - 20 set 2018
 *
 */
public class LazyInitMap<K, V> extends AbstractMapDecorator<K, V> implements Serializable {

	@SuppressWarnings("rawtypes")
	private static final Map NO_INIT = Collections.emptyMap();
	/**
	 * 
	 */
	private static final long serialVersionUID = 3106037526354302971L;
	private transient Supplier<? extends Map<K, V>> factory;

	@SuppressWarnings("unchecked")
	private transient Map<K, V> mapz = NO_INIT;

	// -----------------------------------------------------------------------

	/**
	 * @param factory
	 *            the factory to use, must not be null
	 * @throws NullPointerException
	 *             if map or factory is null
	 */
	public LazyInitMap(@NonNull final Supplier<? extends Map<K, V>> factory) {
		super();
		this.factory = factory;
	}

	// -----------------------------------------------------------------------

	/*
	 * inizializzazione al primo accesso alla mappa tramite Supplier definito da
	 * costruttore (non-Javadoc)
	 * @see org.apache.commons.collections4.map.AbstractMapDecorator#decorated()
	 */
	@Override
	protected Map<K, V> decorated() {
		// use a temporary variable to reduce the number of reads of the volatile field
		Map<K, V> result = mapz;
		if (result == NO_INIT) {
			synchronized (this) {
				// effettuo un ulteriore controllo
				result = mapz;
				if (result == NO_INIT) {
					mapz = result = factory.get();
				}
			}
		}
		return result;
	}

	/**
	 * @param key
	 * @param forceReload
	 * @return
	 */
	public V get(final Object key, boolean forceReload) {
		if (forceReload) {
			reload();
		}
		return get(key);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void reload() {
		synchronized (this) {
			mapz = NO_INIT;
		}
	}

	/**
	 * @return
	 */
	public Map<K, V> getMap() {
		return UnmodifiableMap.unmodifiableMap(decorated());
	}

	/**
	 * @param forceReload
	 * @return
	 */
	public Map<K, V> getMap(boolean forceReload) {
		if (forceReload) {
			reload();
		}
		return getMap();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.collections4.map.AbstractMapDecorator#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

}
