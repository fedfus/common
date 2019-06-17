package com.fedfus.common.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.ObjectUtils;

public class NumberUtils {

	/**
	 * @param number
	 * @param targetClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <O extends Number> O convertObjectToNumberClass(Object number, Class<O> numberClass) {
		if (number instanceof Number) {
			return (O) convertNumberToTargetClass((Number) number, numberClass);
		}
		return null;
	}

	/**
	 * @param number
	 * @param targetClass
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Number convertNumberToTargetClass(Number number, Class<?> targetClass) throws IllegalArgumentException {
		if (targetClass == null || targetClass.isInstance(number)) {
			return number;
		} else if (targetClass.equals(Byte.class)) {
			long value = number.longValue();
			if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return number.byteValue();
		} else if (targetClass.equals(Short.class)) {
			long value = number.longValue();
			if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return number.shortValue();
		} else if (targetClass.equals(Integer.class)) {
			long value = number.longValue();
			if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
				raiseOverflowException(number, targetClass);
			}
			return number.intValue();
		} else if (targetClass.equals(Long.class)) {
			return number.longValue();
		} else if (targetClass.equals(Float.class)) {
			return number.floatValue();
		} else if (targetClass.equals(Double.class)) {
			return number.doubleValue();
		} else if (targetClass.equals(BigInteger.class)) {
			return BigInteger.valueOf(number.longValue());
		} else if (targetClass.equals(BigDecimal.class)) {
			// se l'input e' bigdecimal ed il risultato atteso e' bigdecimal allora restituisco direttamente il valore
			if (BigDecimal.class.isAssignableFrom(number.getClass())) {
				return number;
			}
			// using BigDecimal(String) here, to avoid unpredictability of BigDecimal(double)
			// (see BigDecimal javadoc for details)
			return new BigDecimal(number.toString());
		} else {
			throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass()
																											 .getName() + "] to unknown target class [" + targetClass.getName() + "]");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Number> T convertNumberToTargetNumberClass(Number number, Class<T> targetClass) {
		return (T) convertNumberToTargetClass(number, targetClass);
	}

	/**
	 * @param number
	 * @param targetClass
	 */
	private static void raiseOverflowException(Number number, Class<?> targetClass) {
		throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass()
																										 .getName() + "] to target class [" + targetClass.getName() + "]: overflow");
	}

	/**
	 * @param a
	 * @param b
	 * @return (a != null ? a : 0) + (b != null ? b : 0)
	 */
	public static BigDecimal sumNullSafe(BigDecimal a, BigDecimal b) {
		return (ObjectUtils.defaultIfNull(a, BigDecimal.ZERO).add(ObjectUtils.defaultIfNull(b, BigDecimal.ZERO)));
	}

}
