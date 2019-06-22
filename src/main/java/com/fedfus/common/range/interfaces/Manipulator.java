/**
 * 
 */
package com.fedfus.common.range.interfaces;

import java.util.function.Function;

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

	/**
	 * @param increase
	 *            function
	 * @param decrease
	 *            function
	 * @return
	 */
	public static <M> Manipulator<M> of(Function<M, M> increase, Function<M, M> decrease) {
		return new Manipulator<M>() {

			@Override
			public M increase(M input) {
				return increase.apply(input);
			}

			@Override
			public M decrease(M input) {
				return decrease.apply(input);
			}
		};

	}
}
