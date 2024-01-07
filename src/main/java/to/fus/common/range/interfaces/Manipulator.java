/**
 * 
 */
package to.fus.common.range.interfaces;

import java.util.function.UnaryOperator;

/**
 * @author F.Fusto - 15 giu 2018
 * 
 *         Permette di manipolare un oggetto incrementando o decrementando il
 *         valore
 *
 * @param <T>
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
	public static <M> Manipulator<M> of(UnaryOperator<M> increase, UnaryOperator<M> decrease) {
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
