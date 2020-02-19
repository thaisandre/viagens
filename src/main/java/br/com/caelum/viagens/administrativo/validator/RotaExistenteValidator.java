package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;

public class RotaExistenteValidator implements Validator {

	private RotaRepository rotaRepository;

	public RotaExistenteValidator(RotaRepository rotaRepository) {
		this.rotaRepository = rotaRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NewRotaInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewRotaInputDto newRotaDto = (NewRotaInputDto) target;

		if(newRotaDto.getOrigemId() != null && newRotaDto.getDestinoId() != null) {
			Optional<Rota> rotaBuscada = this.rotaRepository
					.findByOrigemIdAndDestinoId(newRotaDto.getOrigemId(), newRotaDto.getDestinoId());
			if (rotaBuscada.isPresent()) {
				errors.reject("Unique.newRotaInputDto", null, "Rota já existe no sistema.");
			}
		}
	}

}
