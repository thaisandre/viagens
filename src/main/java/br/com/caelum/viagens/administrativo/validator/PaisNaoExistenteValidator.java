package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class PaisNaoExistenteValidator implements Validator {

	@Autowired
	private PaisRepository paisRepository;

	public PaisNaoExistenteValidator(PaisRepository paisRepository) {
		this.paisRepository = paisRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NewCompanhiaInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewCompanhiaInputDto newCompanhiaInput = (NewCompanhiaInputDto) target;
		if(newCompanhiaInput.getPaisId() != null) {
			Optional<Pais> paisBuscado = this.paisRepository.findById(newCompanhiaInput.getPaisId());

			if(!paisBuscado.isPresent()) {
				errors.rejectValue("paisId", null, "País não existe no sistema.");
			}
		}
	}

}
