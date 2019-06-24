package com.fedfus.common.range.utils;

import java.util.function.UnaryOperator;

import org.apache.commons.lang3.ObjectUtils;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author federico - Jun 22, 2019
 *
 * @param <N>
 */
@Getter
@RequiredArgsConstructor
public class NumericInterval<N extends Number & Comparable<N>> implements Intervals<N> {

	@NonNull
	private N start;
	@NonNull
	private N end;
	@NonNull
	private UnaryOperator<N> increaser;
	@NonNull
	private UnaryOperator<N> decreaser;

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
