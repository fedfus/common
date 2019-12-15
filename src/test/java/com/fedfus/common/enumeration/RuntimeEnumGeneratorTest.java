package com.fedfus.common.enumeration;

import com.fedfus.common.enumeration.interfaces.EnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

class RuntimeEnumGeneratorTest {

	@Test
	@DisplayName("Test aggiunta nuovo valore ad enum esistente.")
	void generateEnum() throws Exception {
		final String newValue = "tree";
		RuntimeEnumGenerator.generateEnum(TestEnum.class, newValue);
		Assertions.assertEquals(3, TestEnum.values().length, Arrays.asList(TestEnum.values()).toString());
		Assertions.assertTrue(Stream.of(TestEnum.values()).anyMatch(en -> newValue.equals(en.getCode())));
	}

	@AllArgsConstructor
	@Getter
	enum TestEnum implements EnumCode<String> {
		UNO("one"),
		DUE("two");
		private final String code;
	}
}