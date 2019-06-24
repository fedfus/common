package com.fedfus.common.range.interfaces;

/**
 * @author F.Fusto
 *
 * @param <T>
 */
public interface Interval<T> {

	/**
	 * @return interval start
	 */
	T getStart();

	/**
	 * @return interval end
	 */
	T getEnd();

}
