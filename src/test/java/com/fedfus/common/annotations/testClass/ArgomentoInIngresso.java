/**
 * 
 */
package com.fedfus.common.annotations.testClass;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Retention(RUNTIME)
@Target(FIELD)
/**
 * @author F.Fusto - 11 dic 2018
 *
 */
public @interface ArgomentoInIngresso {

	/**
	 * Nome argomento in ingresso
	 * 
	 * @return
	 */
	public String value();

	/**
	 * true se il parametro è obbligatorio
	 * 
	 * @return
	 */
	public boolean obbligatorio() default true;

	/**
	 * Se true, esegue l'eventuale injection del valore anche se il campo non è null.
	 * 
	 * @return
	 */
	public boolean replaceIfNotNull() default true;

	/**
	 * Se true, viene eseguito l'inject anche del valore <code>null</code>
	 * 
	 * @return
	 */
	public boolean injectNullValue() default false;

	/**
	 * Se true, viene eseguito l'inject anche di un valore <code>vuoto</code> (es. String blank, Collection vuota)
	 * 
	 * @return
	 */
	public boolean injectEmptyValue() default false;
}
