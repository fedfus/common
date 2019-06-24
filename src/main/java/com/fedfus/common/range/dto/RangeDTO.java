/**
 * 
 */
package com.fedfus.common.range.dto;

import java.util.Comparator;

/**
 * @author F.Fusto - 14 giu 2018
 *
 */
public class RangeDTO<T> implements Comparable<T> {

	private T rangeStart;
	private T rangeEnd;

	private Comparator<T> comparator;

	/**
	 * default
	 */
	public RangeDTO() {

	}

	/**
	 * @param rangeStart
	 * @param rangeEnd
	 */
	public RangeDTO(T rangeStart, T rangeEnd, Comparator<T> comparator) {
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
		this.comparator = comparator;
	}

	/**
	 * @return the rangeStart
	 */
	public T getRangeStart() {
		return rangeStart;
	}

	/**
	 * @param rangeStart
	 *            the rangeStart to set
	 */
	public void setRangeStart(T rangeStart) {
		this.rangeStart = rangeStart;
	}

	/**
	 * @return the rangeEnd
	 */
	public T getRangeEnd() {
		return rangeEnd;
	}

	/**
	 * @param rangeEnd
	 *            the rangeEnd to set
	 */
	public void setRangeEnd(T rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(T o) {
		return comparator.compare(this.rangeStart, o);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.rangeStart + " -> " + this.rangeEnd;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
