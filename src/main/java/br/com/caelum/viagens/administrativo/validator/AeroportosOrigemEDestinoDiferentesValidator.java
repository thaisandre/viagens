package br.com.caelum.viagens.administrativo.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;

public class AeroportosOrigemEDestinoDiferentesValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return NewRotaInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewRotaInputDto newRotaDto = (NewRotaInputDto) target;

		if (newRotaDto.getOrigemId() != null && newRotaDto.getDestinoId() != null) {
			if (newRotaDto.getOrigemId().equals(newRotaDto.getDestinoId())) {
				errors.reject("NotEqual.newRotainputDto.origemEDestino", null,
						"Aeroportos de origem e destino não podem ser iguais.");
			}
		}

	}

}
