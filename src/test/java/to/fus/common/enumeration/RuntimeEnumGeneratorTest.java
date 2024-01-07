package to.fus.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import to.fus.common.enumeration.interfaces.EnumCode;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class RuntimeEnumGeneratorTest {
private final Logger log = Logger.getLogger(this.getClass().getName());

	@Test
	@DisplayName("Test aggiunta nuovo valore ad enum esistente.")
	void generateEnum() throws Exception {
		final String newValue = "tree";
		log.info("Base Enum: " + Stream.of(TestEnum.values()).collect(Collectors.toList()));
		RuntimeEnumGenerator.generateEnum(TestEnum.class, newValue);
		Assertions.assertEquals(3, TestEnum.values().length, Arrays.asList(TestEnum.values()).toString());
		Assertions.assertTrue(Stream.of(TestEnum.values()).anyMatch(en -> newValue.equals(en.getCode())));
		log.info("Modified Enum: " + Stream.of(TestEnum.values()).collect(Collectors.toList()));
	}

	@AllArgsConstructor
	@Getter
	enum TestEnum implements EnumCode<String> {
		UNO("one"), DUE("two");
		private final String code;
	}
}