package br.com.caelum.viagens.voos.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class CompanhiaExistenteValidator implements Validator{
	
	private CompanhiaRepository companhiaRepository;

	public CompanhiaExistenteValidator(CompanhiaRepository companhiaRepository) {
		this.companhiaRepository = companhiaRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return NewVooInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewVooInputDto newVoo = (NewVooInputDto) target;
		
		Optional<Companhia> companhiaBuscada = companhiaRepository.findById(newVoo.getCompanhiaId());
		if(!companhiaBuscada.isPresent()) {
			errors.rejectValue("companhiaId", null, "Companhia não existe no sistema.");
		}
	}

}
