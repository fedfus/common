package to.fus.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import to.fus.common.numbers.NumberUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.Function;

/**
 * @author F.Fusto
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utility {

	/**
	 * @param input
	 * @param transformer
	 * @param defaultValue
	 * @return
	 */
	public static <I, O> O defaultIfNull(I input, Function<I, O> transformer, O defaultValue) {
		if (input == null) {
			return defaultValue;
		}
		return transformer.apply(input);
	}

	/**
	 * Converte la stringa passata in input in un BigDecimal (se valida)
	 * 
	 * @param input
	 * @param defaultValue
	 * @return
	 */
	public static BigDecimal defaultIfNull(String input, BigDecimal defaultValue) {
		return defaultIfNull(StringUtils.trimToNull(input), in -> StringUtils.isNumeric(in) ? new BigDecimal(in) : defaultValue, defaultValue);
	}

	/**
	 * @param input
	 * @param defaultValue
	 * @return
	 */

	public static <O extends Number> O defaultNullIfNull(String input, Class<O> returnClass) {
		O nullObject = null;
		return defaultIfNull(input, nullObject, returnClass);
	}

	/**
	 * @param input
	 * @param transformer
	 * @return
	 */
	public static <I, O> O defaultNullIfNull(I input, Function<I, O> transformer) {
		return defaultIfNull(input, transformer, null);
	}

	/**
	 * @param input
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <O extends Number> O defaultIfNull(String input, O defaultValue) {
		return defaultIfNull(input, defaultValue, (Class<O>) defaultValue.getClass());
	}

	/**
	 * @param input
	 * @param defaultValue
	 * @param returnClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <O extends Number> O defaultIfNull(String input, O defaultValue, Class<O> returnClass) {
		return (O) defaultIfNull(StringUtils.trimToNull(input), in -> {
			if (StringUtils.isNumeric(in)) {
				try {
					return NumberUtils.convertNumberToTargetClass(NumberFormat.getInstance().parse(in), returnClass);
				} catch (IllegalArgumentException | ParseException e) {
					return defaultValue;
				}
			} else {
				return defaultValue;
			}
		}, defaultValue);
	}

	/**
	 * Metodo che dati in input degli object, restituisce true se almeno uno � non nullo,
	 * false se sono tutti null.
	 * 
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean almostOneNotNull(Object... objects) {
		Object o = ObjectUtils.firstNonNull(objects);
		return (o != null);
	}
}
