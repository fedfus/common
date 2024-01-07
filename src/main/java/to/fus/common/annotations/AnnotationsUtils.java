/**
 * 
 */
package to.fus.common.annotations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import to.fus.common.numbers.NumberUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author F.Fusto - 11 dic 2018
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationsUtils {

	/**
	 * BiFunction per estrarre il valore da un field annotato
	 */
	private static final BiFunction<Field, Object, ?> defaultValueExtractor = (field, clazz) -> {
		try {
			field.setAccessible(true);
			return field.get(clazz);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// log errore
		}
		return null;
	};

	/**
	 * Predicate utilizzato per verificare se
	 * <ul>
	 * <li>una collection è vuota</li>
	 * <li>una String è null/blank</li>
	 * </ul>
	 */
	private static final Predicate<Object> emptyValueChecker = value -> {
		if (value instanceof Collection) {
			return ((Collection<?>) value).isEmpty();
		} else if (value instanceof String) {
			return StringUtils.isBlank((String) value);
		} else {
			return value == null;
		}
	};

	/**
	 * @param mainClass
	 * @param annotation
	 * @param parametri
	 * @param paramNameExtractor
	 * @return
	 */
	public static <T extends Annotation> boolean injectParameter(Object mainClass, Class<T> annotation, Function<T, String> paramNameExtractor, BiFunction<String, Field, ?> valueExtractor, Predicate<T> replaceIfNotNull, Predicate<T> injectNullValue, Predicate<T> injectEmptyValue) {
		for (Field field : getAnnotatedField(mainClass.getClass(), annotation)) {
			T parametro = field.getAnnotation(annotation);
			if (!(!replaceIfNotNull.test(parametro) && defaultValueExtractor.apply(field, mainClass) != null)) {
				String paramName = paramNameExtractor.apply(parametro);
				Object value = valueExtractor.apply(paramName, field);
				if (!((value == null && !injectNullValue.test(parametro)) || (!injectEmptyValue.test(parametro) && emptyValueChecker.test(value)))) {
					injectParameter(mainClass, field, value);
				}
			}
		}
		return true;
	}

	/**
	 * @param mainClass
	 * @param field
	 * @param value
	 * @return
	 */
	public static boolean injectParameter(Object mainClass, Field field, Object value) {
		if (mainClass != null && field != null) {
			try {
				field.setAccessible(true);
				if (field.getType().isPrimitive() && value == null) {
					return false;
				}
				FieldUtils.writeField(field, mainClass, value, true);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * @param mainClass
	 * @param annotation
	 * @param paramNameExtractor
	 * @param parameterName
	 * @param value
	 * @return
	 */
	public static <T extends Annotation> boolean injectParameter(@NonNull Object mainClass, @NonNull final Class<T> annotation, @NonNull Function<T, String> paramNameExtractor, @NonNull String parameterName, Object value) {
		Field inj = getAnnotatedField(mainClass.getClass(), annotation).stream()
																	   .filter(f -> StringUtils.equalsIgnoreCase(paramNameExtractor.apply(f.getAnnotation(annotation)), parameterName))
																	   .findFirst()
																	   .orElse(null);
		return injectParameter(mainClass, inj, value);
	}

	/**
	 * @param mainClass
	 * @param annotation
	 * @param paramNameExtractor
	 * @return
	 */
	public static <T, A extends Annotation> List<T> getParameterKeyFromAnnotation(Object mainClass, Class<A> annotation, Function<A, T> paramNameExtractor) {
		return getParameterKeyFromAnnotation(mainClass, annotation, paramNameExtractor, x -> true);
	}

	/**
	 * @param mainClass
	 * @param annotation
	 * @param paramNameExtractor
	 * @param obbligatorio
	 * @return
	 */
	public static <T, A extends Annotation> List<T> getParameterKeyFromAnnotation(Object mainClass, Class<A> annotation, Function<A, T> paramNameExtractor, Predicate<A> obbligatorio) {
		final List<T> result = new ArrayList<>();
		getAnnotatedField(mainClass.getClass(), annotation).forEach(field -> {
			A parametro = field.getAnnotation(annotation);
			T value = paramNameExtractor.apply(parametro);
			if (value != null && obbligatorio.test(parametro)) {
				result.add(value);
			}
		});
		return result;
	}

	/**
	 * @param mainClass
	 * @param class1
	 * @param object
	 */
	public static <T extends Annotation, V> Map<String, V> getParameterValuesFromAnnotation(Object mainClass, Class<T> annotation, Function<T, String> paramNameExtractor, Predicate<T> filter, BiFunction<Field, Object, V> valueExtractor) {
		Map<String, V> values = new HashMap<>();
		getAnnotatedField(mainClass.getClass(), annotation).forEach(field -> {
			T parametro = field.getAnnotation(annotation);
			if (filter.test(parametro)) {
				String paramName = paramNameExtractor.apply(parametro);
				field.setAccessible(true);
				V value = valueExtractor.apply(field, mainClass);
				values.put(paramName, value);
			}
		});
		return values;
	}

	/**
	 * @param mainClass
	 * @param annotation
	 * @param paramNameExtractor
	 * @return
	 */
	public static <T extends Annotation> Map<String, ?> getParameterValuesFromAnnotation(Object mainClass, Class<T> annotation, Function<T, String> paramNameExtractor, Predicate<T> filter) {
		return getParameterValuesFromAnnotation(mainClass, annotation, paramNameExtractor, filter, defaultValueExtractor);
	}

	/**
	 * @param mainClass
	 * @param annotation
	 * @return
	 */
	public static <T extends Annotation> List<Field> getAnnotatedField(@NonNull Class<?> mainClass, @NonNull Class<T> annotation) {
		return FieldUtils.getAllFieldsList(mainClass).stream().filter(f -> f.isAnnotationPresent(annotation)).collect(Collectors.toList());
	}

	/**
	 * @param argument
	 * @param fieldType
	 * @return
	 */
	public static Object getValueFromArgument(String value, Class<?> fieldType) {
		fieldType = fieldType.isPrimitive() ? ClassUtils.primitiveToWrapper(fieldType) : fieldType;
		String trimmedArgument = StringUtils.trim(value);
		Object retValue = null;
		if (Number.class.isAssignableFrom(fieldType)) {
			retValue = NumberUtils.convertNumberToTargetClass(new BigDecimal(trimmedArgument), fieldType);
		} else {
			retValue = trimmedArgument;
		}
		return retValue;
	}

}
