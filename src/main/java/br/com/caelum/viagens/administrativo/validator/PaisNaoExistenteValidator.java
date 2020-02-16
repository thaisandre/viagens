package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.PossuiPaisDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class PaisNaoExistenteValidator implements Validator {

	private PaisRepository paisRepository;

	public PaisNaoExistenteValidator(PaisRepository paisRepository) {
		this.paisRepository = paisRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return PossuiPaisDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PossuiPaisDto possuiPaisDto = (PossuiPaisDto) target;

		if (possuiPaisDto.getPaisId() != null) {
			Optional<Pais> paisBuscado = this.paisRepository.findById(possuiPaisDto.getPaisId());

			if (!paisBuscado.isPresent()) {
				errors.rejectValue("paisId", null, "País não existe no sistema.");
			}
		}
	}

}
