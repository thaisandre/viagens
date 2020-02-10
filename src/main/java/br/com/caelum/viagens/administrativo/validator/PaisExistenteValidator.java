package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewPaisInputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class PaisExistenteValidator implements Validator{
	
	@Autowired
	private PaisRepository paisRepository;
	
	public PaisExistenteValidator(PaisRepository paisRepository) {
		this.paisRepository = paisRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NewPaisInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewPaisInputDto paisInputDto = (NewPaisInputDto) target;
		
		Optional<Pais> paisBuscado = this.paisRepository.findByNome(paisInputDto.getNome());
		if(paisBuscado.isPresent()) {
			errors.rejectValue("nome", null, "Pais já existe no sistema.");
		}
	}

}
