package com.fedfus.common.enumeration;

import com.fedfus.common.enumeration.interfaces.EnumCode;
import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.reflect.Modifier.FINAL;

/**
 * @author F.Fusto 04/lug/2017
 * @implSpec Estensione dell'implementazione originale  https://www.niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically
 */
public final class RuntimeEnumGenerator {

	/**
	 * Permette di aggiungere valori ad un Enum esistente che implementa
	 * l'interfaccia <code>EnumCode</code> a Runtime
	 *
	 * @param clazz
	 * 		T extends EnumCode<?>
	 * @param value
	 * 		da aggiungere
	 *
	 * @return new enum value
	 *
	 * @throws Exception
	 * 		ex
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <X, T extends EnumCode<X>> T generateEnum(final Class<T> clazz, final X value) throws Exception {
		if (!Enum.class.isAssignableFrom(clazz)) {
			throw new RuntimeException("class " + clazz + " is not an instance of EnumCode");
		}

		// 1. Cerco "$VALUES" nella enum class in modo da recuperare le enum instances
		// esistenti
		Field valuesField = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().contains("$VALUES")) {
				valuesField = field;
				break;
			}
		}
		// rendo accessibile il field
		AccessibleObject.setAccessible(new Field[] { valuesField }, true);
		// 2. Ottengo i valori contenuti nell'enum e li copio in una lista
		T[] previousValues = valuesField != null ? (T[]) valuesField.get(clazz) : null;

		List values = new ArrayList<T>();
		if (previousValues != null) {
			values.addAll(Arrays.asList(previousValues));
		}

		// 3. costruzione del nuovo enum
		T newValue = (T) makeEnum(clazz, // target enum class
				value != null ? value.toString() : "UNDEFINED",
				// se value non è null assegno come nome del nuovo enum il value.toString(),
				// altrimenti "UNDEFINED"
				values.size(), // enum size, per aggiungere il nuovo valore in coda a $VALUES
				(value != null) ? new Class[] { value.getClass() } : new Class[] {},
				// aggiungo value.class (se value != null) per poter recuperare il costruttore
				// dell'enum
				(value != null) ? new Object[] { value } : new Object[] {}); // se value!= null, aggiungo value
		// all'array in modo da poterlo passare
		// al costruttore

		// 4. aggiungo il nuovo enum appena creato alla lista
		values.add(newValue);
		// 5. Set new values field
		setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(clazz, 0)));
		// 6. Clean enum cache
		cleanEnumCache(clazz);

		return newValue;
	}

	/****************************************************************
	 ** METODI PRIVATI
	 ****************************************************************/

	private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
		blankField(enumClass, "enumConstantDirectory"); // JDK 1.5/6
		blankField(enumClass, "enumConstants"); // IBM JDK
	}

	private static void blankField(Class<?> enumClass, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		for (Field field : Class.class.getDeclaredFields()) {
			if (field.getName().contains(fieldName)) {
				AccessibleObject.setAccessible(new Field[] { field }, true);
				setFailsafeFieldValue(field, enumClass, null);
				break;
			}
		}
	}

	private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes,
			Object[] additionalValues) throws Exception {
		Object[] parms = new Object[additionalValues.length + 2];
		parms[0] = value;
		parms[1] = ordinal;
		System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
		Constructor<?> constructor = getConstructor(enumClass, additionalTypes);
		if (constructor == null) {
			throw new IllegalArgumentException(
					"Impossibile ricavare un costruttore per generare un nuovo valore nell'Enum " + enumClass
							.getCanonicalName());
		}
		ConstructorAccessor costruttore = ReflectionFactory.getReflectionFactory().newConstructorAccessor(constructor);
		Object instance;
		try {
			instance = costruttore.newInstance(parms);
		} catch (IllegalArgumentException ex) {
			instance = costruttore.newInstance(Arrays.copyOf(parms, constructor.getParameterTypes().length));
		}
		return enumClass.cast(instance);
	}

	private static Constructor<?> getConstructor(Class<?> enumClass, Class<?>[] additionalParameterTypes) {
		Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
		parameterTypes[0] = String.class;
		parameterTypes[1] = int.class;
		System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
		Constructor<?> minNumParamConstr = null;
		for (Constructor<?> constr : enumClass.getDeclaredConstructors()) {
			if ((minNumParamConstr == null || (minNumParamConstr.getParameterTypes().length > constr
					.getParameterTypes().length))// confronto
					// il numero
					// dei
					// parametri
					&& checkConstructorParameters(constr)) {
				minNumParamConstr = constr;
			}
			if (Arrays.equals(parameterTypes, constr.getParameterTypes())) {
				return constr;
			}
		}
		// se si arriva a questo punto vuol dire che non è presente il costruttore
		// cercato, perciò provo ad utilizzare il costruttore con il minor numero di
		// parametri
		return minNumParamConstr;
	}

	private static boolean checkConstructorParameters(Constructor<?> constr) {
		// effettuo i controlli per verificare se il costruttore "constr" ha un numero
		// di parametri > 2 (per evitare il costruttore di default).
		return (constr.getParameterTypes().length >= 2) // deve contenere almeno due parametri
				&& (String.class.equals(constr.getParameterTypes()[0]) && int.class
				.equals(constr.getParameterTypes()[1])) // controllo che i due parametri siano
				// quelli di default della classe ENUM
				;
	}

	private static void setFailsafeFieldValue(Field field, Object target, Object value)
			throws NoSuchFieldException, IllegalAccessException {
		// let's make the field accessible
		field.setAccessible(true);

		// next we change the modifier in the Field instance to
		// not be final anymore, thus tricking reflection into
		// letting us modify the static final field
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);

		// blank out the final bit in the modifiers int
		modifiers &= ~FINAL;
		modifiersField.setInt(field, modifiers);
		FieldAccessor fa = ReflectionFactory.getReflectionFactory().newFieldAccessor(field, false);
		fa.set(target, value);
	}
}