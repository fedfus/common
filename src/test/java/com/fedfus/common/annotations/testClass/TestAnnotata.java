package com.fedfus.common.annotations.testClass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestAnnotata {

	@ArgomentoInIngresso("prova")
	private String provaAnnotazione;

}
