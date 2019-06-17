/**
 * 
 */
package com.fedfus.common.range.interfaces;

/**
 * @author F.Fusto - 15 giu 2018
 * 
 *         Permette di manipolare un oggetto incrementando o decrementando il
 *         valore
 *
 */
public interface Manipulator<T> {
	/**
	 * @param input
	 * @return input incrementato
	 */
	public T increase(T input);

	/**
	 * @param input
	 * @return input decrementato
	 */
	public T decrease(T input);
}
