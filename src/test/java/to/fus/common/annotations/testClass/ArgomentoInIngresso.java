/**
 *
 */
package to.fus.common.annotations.testClass;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Retention(RUNTIME)
@Target(FIELD)
/**
 * @author F.Fusto - 11 dic 2018
 *
 */ public @interface ArgomentoInIngresso {

    /**
     * Nome argomento in ingresso
     *
     * @return
     */
    String value();

    /**
     * true se il parametro è obbligatorio
     *
     * @return
     */
    boolean obbligatorio() default true;

    /**
     * Se true, esegue l'eventuale injection del valore anche se il campo non è null.
     *
     * @return
     */
    boolean replaceIfNotNull() default true;

    /**
     * Se true, viene eseguito l'inject anche del valore <code>null</code>
     *
     * @return
     */
    boolean injectNullValue() default false;

    /**
     * Se true, viene eseguito l'inject anche di un valore <code>vuoto</code> (es. String blank, Collection vuota)
     *
     * @return
     */
    boolean injectEmptyValue() default false;
}
