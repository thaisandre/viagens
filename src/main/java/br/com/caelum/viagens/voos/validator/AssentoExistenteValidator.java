package br.com.caelum.viagens.voos.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.aeronaves.model.Assento;
import br.com.caelum.viagens.aeronaves.repository.AssentoRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;

public class AssentoExistenteValidator implements Validator{
	
	private AssentoRepository assentoRepository;
	
	public AssentoExistenteValidator(AssentoRepository assentoRepository) {
		this.assentoRepository = assentoRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return NewPassagemInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewPassagemInputDto newPassagemDto = (NewPassagemInputDto) target;
		
		if(!errors.hasErrors()) {
			Optional<Assento> assentoBuscado = assentoRepository.findById(newPassagemDto.getAssentoId());
			
			if(!assentoBuscado.isPresent()) {
				errors.rejectValue("assentoId", null, "assento não existe no sistema.");
			} 
		}
	}
}
