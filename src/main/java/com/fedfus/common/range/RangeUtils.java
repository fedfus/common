/**
 * 
 */
package com.fedfus.common.range;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

import org.apache.commons.lang3.Range;

import com.fedfus.common.range.interfaces.Manipulator;

/**
 * @author F.Fusto - 14 giu 2018
 *
 */
public class RangeUtils<T> {

	private TreeSet<Range<T>> rangeList;
	private Manipulator<T> manipulator;
	private Comparator<T> comparator;

	/**
	 * @param manipulator
	 * @param comparator
	 */
	private RangeUtils(Manipulator<T> manipulator, Comparator<T> comparator) {
		this.manipulator = manipulator;
		this.comparator = comparator;
		rangeList = new TreeSet<>((o1, o2) -> this.comparator.compare(o1.getMinimum(), o2.getMinimum()));
	}

	/**
	 * @param simple
	 *            range
	 * @param manipulator
	 */
	public RangeUtils(Range<T> range, Manipulator<T> manipulator) {
		this(manipulator, range.getComparator());
		rangeList.add(range);
	}

	/**
	 * @param multiple
	 *            range
	 * @param manipulator
	 *            - utilizzato per incrementare o decrementare il range
	 * @param comparator
	 */
	public RangeUtils(List<Range<T>> range, Manipulator<T> manipulator, Comparator<T> comparator) {
		this(manipulator, comparator);
		if (range != null) {
			for (Range<T> r : range) {
				if (r != null) {
					rangeList.add(r);
				}
			}
		}
	}

	/**
	 * @param range
	 * @param transformer
	 *            - permette di trasforamre la lista passata in ingresso
	 * @param manipulator
	 *            - utilizzato per incrementare o decrementare il range
	 * 
	 * @param comparator
	 */
	public <X> RangeUtils(List<X> range, Function<X, Range<T>> transformer, Manipulator<T> manipulator, Comparator<T> comparator) {
		this(manipulator, comparator);
		if (range != null) {
			for (X r : range) {
				if (r != null) {
					rangeList.add(transformer.apply(r));
				}
			}
		}
	}

	/**
	 * @param elem
	 * @return
	 */
	public boolean isInRange(T elem) {
		return ricerca(elem) != null;
	}

	/**
	 * restituisce il primo elemento disponivile senza rimuoverlo
	 * 
	 * @return
	 */
	public T getFirstAvailableValue() {
		// implementazione che tiene conto della struttura dati utilizzata per
		// conservare i range (TreeSet)
		if (hasAvailableValues()) {
			return rangeList.first().getMinimum();
		}
		return null;
	}

	/**
	 * Rimuove e restituisce il primo elemento disponibile nel range
	 * 
	 * @return
	 */
	public T removeFirstAvailableValue() {
		T result = null;
		if (hasAvailableValues()) {
			Range<T> first = rangeList.first();
			result = first.getMinimum();
			Comparator<T> rangeComp = first.getComparator();
			// caso di range formato dallo stesso valore per minimo e massimo
			rangeList.remove(first);
			if (rangeComp.compare(first.getMinimum(), first.getMaximum()) != 0) {
				// +1 del minimum
				Range<T> firstPiuUno = Range.between(manipulator.increase(first.getMinimum()), first.getMaximum(), rangeComp);
				rangeList.add(firstPiuUno);
			}

		}
		return result;
	}

	/**
	 * @return
	 */
	public boolean hasAvailableValues() {
		return !rangeList.isEmpty();
	}

	/**
	 * @param elem
	 * @return
	 */
	public T removeElement(T elem) {
		Range<T> daRimuovere = ricerca(elem);
		if (daRimuovere != null) {
			rangeList.remove(daRimuovere);

			Comparator<T> rangeComp = daRimuovere.getComparator();
			if (rangeComp.compare(daRimuovere.getMinimum(), daRimuovere.getMaximum()) != 0) {

				Range<T> firstPiuUno = Range.between(manipulator.increase(elem), daRimuovere.getMaximum(), rangeComp);
				Range<T> firstMenoUno = Range.between(daRimuovere.getMinimum(), manipulator.decrease(elem), rangeComp);
				// se e' il primo
				if (rangeComp.compare(elem, daRimuovere.getMinimum()) == 0) {
					rangeList.add(firstPiuUno);
					return elem;
				}

				// se e' l'ultimo
				if (rangeComp.compare(elem, daRimuovere.getMaximum()) == 0) {
					rangeList.add(firstMenoUno);
					return elem;
				}

				// se e' tra i due range
				rangeList.add(firstPiuUno);
				rangeList.add(firstMenoUno);
			}
			return elem;

		}
		return null;
	}

	/**
	 * @param elem
	 * @param returnAlternativeValueIfNotAvailable
	 *            specifica se restituire un valore
	 *            alternativo se l'elem cercato non
	 *            e' disponibile
	 * @return
	 *         <li><code>elem</code> se e' disponibile nel range</li>
	 *         <li>altrimenti
	 *         <ul>
	 *         <li>se parametro
	 *         <code>returnAlternativeValueIfNotAvailable == true</code> restituisce
	 *         il primo <code>elem</code> disponibile
	 *         {@link #removeFirstAvailableValue()}</li>
	 *         <li>se parametro
	 *         <code>returnAlternativeValueIfNotAvailable == true</code> restituisce
	 *         <code>null</code></li>
	 *         </ul>
	 *         </li>
	 */
	public T getElementFromRange(T elem, boolean returnAlternativeValueIfNotAvailable) {
		T result = removeElement(elem);
		if (result != null) {
			return result;
		}
		return returnAlternativeValueIfNotAvailable ? removeFirstAvailableValue() : null;
	}

	/**
	 * @param elem
	 * @return
	 *         <ul>
	 *         <li><code>elem</code> se e' disponibile nel range</li>
	 *         <li>altrimenti il primo <code>elem</code> disponibile
	 *         {@link #removeFirstAvailableValue()}</li>
	 *         </ul>
	 */
	public T getElementFromRange(T elem) {
		return getElementFromRange(elem, true);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return rangeList.toString();
	}

	/**********************************************************************************************************************/

	/**
	 * @param elem
	 * @return
	 */
	private Range<T> ricerca(T elem) {
		Range<T> daTrovare = Range.between(elem, elem, comparator);
		Range<T> floor = rangeList.floor(daTrovare);
		Range<T> ceiling = rangeList.ceiling(daTrovare);
		if (ceiling != null && ceiling.contains(elem)) {
			return ceiling;
		}
		if (floor != null && floor.contains(elem)) {
			return floor;
		}
		return null;
	}

}
