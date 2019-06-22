package com.fedfus.common.range.utils;

import java.util.Comparator;
import java.util.function.Function;

import org.apache.commons.lang3.ObjectUtils;

import com.fedfus.common.range.interfaces.Interval;
import com.fedfus.common.range.interfaces.Manipulator;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NumericInterval<N extends Number & Comparable<N>> implements Interval<N>, Manipulator<N>, Comparator<N> {

	@NonNull
	private N start;
	@NonNull
	private N end;
	@NonNull
	private Function<N, N> increaser;
	@NonNull
	private Function<N, N> decreaser;

	/*
	 * (non-Javadoc)
	 * @see com.fedfus.common.range.interfaces.Manipulator#increase(java.lang.Object)
	 */
	@Override
	public N increase(N input) {
		return increaser.apply(input);
	}

	/*
	 * (non-Javadoc)
	 * @see com.fedfus.common.range.interfaces.Manipulator#decrease(java.lang.Object)
	 */
	@Override
	public N decrease(N input) {
		return decreaser.apply(input);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(N o1, N o2) {
		return ObjectUtils.compare(o1, o2);
	}

}
