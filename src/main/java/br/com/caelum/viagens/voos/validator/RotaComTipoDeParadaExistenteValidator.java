package br.com.caelum.viagens.voos.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;
import br.com.caelum.viagens.voos.model.TipoParada;

public class RotaComTipoDeParadaExistenteValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return NewVooInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewVooInputDto newVooDto = (NewVooInputDto) target;

		newVooDto.getRotas().forEach(r -> {
			if (r.getParada() != null) {
				if (r.getParada().getTipo() != null) {
					if (!TipoParada.tiposParada().contains(r.getParada().getTipo())) {
						errors.reject(null, "Tipo de parada \'" + r.getParada().getTipo() + "\' é inválido");
						;
					}
				}
			}
		});
	}
}
