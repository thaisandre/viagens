package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;

public class NomeCompanhiaExistenteValidator implements Validator {

	private CompanhiaRepository companhiaRepository;

	public NomeCompanhiaExistenteValidator(CompanhiaRepository companhiaRepository) {
		this.companhiaRepository = companhiaRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NewCompanhiaInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewCompanhiaInputDto newCompanhiaDto = (NewCompanhiaInputDto) target;

		Optional<Companhia> companhiaBuscada = this.companhiaRepository.findByNome(newCompanhiaDto.getNome());

		if (companhiaBuscada.isPresent()) {
			errors.rejectValue("nome", null, "Companhia de mesmo nome já existe no sistema.");
		}

	}

}
