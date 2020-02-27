package br.com.caelum.viagens.voos;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public class Teste {
	
	public void blah(@NotBlank String nome) {
		
	}
}
