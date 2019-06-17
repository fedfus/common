package com.fedfus.common.annotations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fedfus.common.annotations.testClass.ArgomentoInIngresso;
import com.fedfus.common.annotations.testClass.TestAnnotata;

public class AnnotationTests {

	private TestAnnotata test;
	private Map<String, String> argomenti;
	private Function<Map<String, String>, BiFunction<String, Field, Object>> argumentsValueExtractor;

	@Before
	public void setup() {
		test = new TestAnnotata();
		argomenti = new HashMap<>();
		argomenti.put("prova", "valore1");
		argumentsValueExtractor = argList -> (paramName, field) -> AnnotationsUtils.getValueFromArgument(argList.get(paramName), field.getType());
	}

	@Test
	public void testPreInject() {
		Assert.assertNull(test.getProvaAnnotazione());
	}

	@Test
	public void testInjection() {
		AnnotationsUtils.injectParameter(test, ArgomentoInIngresso.class, ArgomentoInIngresso::value, argumentsValueExtractor.apply(this.argomenti), ArgomentoInIngresso::replaceIfNotNull, ArgomentoInIngresso::injectNullValue, ArgomentoInIngresso::injectEmptyValue);
		Assert.assertEquals("valore1", test.getProvaAnnotazione());
	}
}
