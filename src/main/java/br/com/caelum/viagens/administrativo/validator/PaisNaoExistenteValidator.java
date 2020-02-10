package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

		Optional<Pais> paisBuscado = this.paisRepository.findByNome(newCompanhiaInput.getPais());

		if (StringUtils.hasText(newCompanhiaInput.getPais()) && !paisBuscado.isPresent()) {
			errors.rejectValue("pais", null, "País não existe no sistema.");
		}
	}

}
