package com.fedfus.common.annotations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fedfus.common.annotations.testClass.ArgomentoInIngresso;
import com.fedfus.common.annotations.testClass.TestAnnotata;

public class AnnotationTests {

	private TestAnnotata test;
	private Map<String, String> argomenti;
	private Function<Map<String, String>, BiFunction<String, Field, Object>> argumentsValueExtractor;

	public AnnotationTests() {
		test = new TestAnnotata();
		argomenti = new HashMap<>();
		argomenti.put("prova", "valore1");
		argumentsValueExtractor = argList -> (paramName, field) -> AnnotationsUtils.getValueFromArgument(argList.get(paramName), field.getType());
	}

	@Test
	public void testPreInject() {
		Assertions.assertNull(test.getProvaAnnotazione());
	}

	@Test
	public void testInjection() {
		AnnotationsUtils.injectParameter(test, ArgomentoInIngresso.class, ArgomentoInIngresso::value, argumentsValueExtractor.apply(this.argomenti), ArgomentoInIngresso::replaceIfNotNull, ArgomentoInIngresso::injectNullValue, ArgomentoInIngresso::injectEmptyValue);
		Assertions.assertEquals("valore1", test.getProvaAnnotazione());
	}
}
