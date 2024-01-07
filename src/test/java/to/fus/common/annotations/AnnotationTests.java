package to.fus.common.annotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import to.fus.common.annotations.testClass.ArgomentoInIngresso;
import to.fus.common.annotations.testClass.TestAnnotata;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AnnotationTests {

    public static final String VALORE_1 = "valore1";
    private TestAnnotata test;
    private Map<String, String> argomenti;
    private Function<Map<String, String>, BiFunction<String, Field, Object>> argumentsValueExtractor;

    public AnnotationTests() {
        test = new TestAnnotata();
        argomenti = new HashMap<>();
        argomenti.put("prova", VALORE_1);
        argomenti.put("provaNoNull", null);
        argumentsValueExtractor = argList -> (paramName, field) -> AnnotationsUtils.getValueFromArgument(
                argList.get(paramName), field.getType());
    }

    @Test
    void testPreInject() {
        Assertions.assertNull(test.getProvaAnnotazione());
    }

    @Test
    void testInjection() {
        AnnotationsUtils.injectParameter(test, ArgomentoInIngresso.class, ArgomentoInIngresso::value,
                argumentsValueExtractor.apply(this.argomenti), ArgomentoInIngresso::replaceIfNotNull,
                ArgomentoInIngresso::injectNullValue, ArgomentoInIngresso::injectEmptyValue);
        Assertions.assertEquals(VALORE_1, test.getProvaAnnotazione());
        Assertions.assertNotNull(test.getProvaAnnotazioneNoNull());
    }
}
