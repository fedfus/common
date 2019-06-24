package com.fedfus.common.annotations.testClass;

import lombok.Getter;

/**
 * @author F.Fusto - Jun 22, 2019
 *         <br>
 *         classe di test per verifica injection
 */
@Getter
public class TestAnnotata {

	@ArgomentoInIngresso("prova")
	private String provaAnnotazione;

	@ArgomentoInIngresso(value = "provaNoNull", injectNullValue = false)
	private String provaAnnotazioneNoNull = "valoreNoNull";

}
