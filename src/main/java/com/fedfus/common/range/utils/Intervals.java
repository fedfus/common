package com.fedfus.common.range.utils;

import java.util.Comparator;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.ObjectUtils;

import com.fedfus.common.range.interfaces.Interval;
import com.fedfus.common.range.interfaces.Manipulator;

/**
 * @author federico - Jun 22, 2019
 *
 * @param <T>
 */
public interface Intervals<T> extends Interval<T>, Manipulator<T>, Comparator<T> {

	/**
	 * @param start
	 * @param end
	 * @param increase
	 * @param decrease
	 * @return
	 */
	public static <I extends Comparable<I>> Intervals<I> of(I start, I end, UnaryOperator<I> increase, UnaryOperator<I> decrease) {
		return new Intervals<I>() {

			@Override
			public I getStart() {
				return start;
			}

			@Override
			public I getEnd() {
				return end;
			}

			@Override
			public I increase(I input) {
				return increase.apply(input);
			}

			@Override
			public I decrease(I input) {
				return decrease.apply(input);
			}

			@Override
			public int compare(I o1, I o2) {
				return ObjectUtils.compare(o1, o2);
			}
		};
	}

}
