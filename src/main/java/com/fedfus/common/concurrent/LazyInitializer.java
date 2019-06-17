/**
 * @author F.Fusto - 87000968
 * 
 *         7 giu 2019
 */
package com.fedfus.common.concurrent;

import java.util.function.Supplier;

import org.apache.commons.lang3.concurrent.ConcurrentException;

import lombok.Getter;

/**
 * @author F.Fusto - 87000968
 *
 *         7 giu 2019
 * 
 */
public class LazyInitializer<T> extends org.apache.commons.lang3.concurrent.LazyInitializer<T> {

	@Getter
	private volatile boolean initialized = false;
	private final Supplier<T> initSupplier;

	/**
	 * 
	 */
	public LazyInitializer(Supplier<T> initializer) {
		this.initSupplier = initializer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.lang3.concurrent.LazyInitializer#initialize()
	 */
	@Override
	protected T initialize() throws ConcurrentException {
		return initSupplier.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.lang3.concurrent.LazyInitializer#get()
	 */
	@Override
	public T get() throws ConcurrentException {
		T elem = super.get();
		initialized = true;
		return elem;
	}

}
