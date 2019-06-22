package com.fedfus.common.range.interfaces;

public interface Interval<T> {

	T getStart();

	T getEnd();

	public static <I> Interval<I> of(I start, I end) {
		return new Interval<I>() {

			@Override
			public I getStart() {
				return start;
			}

			@Override
			public I getEnd() {
				return end;
			}
		};
	}

	public static <I extends Comparable<I>> Interval<I> ofComparable(I start, I end) {
		return Interval.of(start, end);
	}

}
