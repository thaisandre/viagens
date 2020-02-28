package br.com.caelum.viagens.voos.validator;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class UltimaRotaPernaFinalValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return NewVooInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewVooInputDto newVooDto = (NewVooInputDto) target;
		
		if(!newVooDto.getRotas().isEmpty()) {
			if(!ultimaRotaIsPernaFinal(newVooDto.getRotas())) {
				errors.reject(null, "A última rota deve ser perna final.");
			}
		}
		
	}

	private boolean ultimaRotaIsPernaFinal(List<NewRotaDoVooInputDto> rotasDoVoo) {
		return rotasDoVoo.get(rotasDoVoo.size() - 1).isPernaFinal();
	}

}
