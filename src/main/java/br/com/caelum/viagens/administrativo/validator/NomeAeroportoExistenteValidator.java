package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;

public class NomeAeroportoExistenteValidator implements Validator{
	
	private AeroportoRepository aeroportoRepository;
	
	public NomeAeroportoExistenteValidator(AeroportoRepository aeroportoRepository) {
		this.aeroportoRepository = aeroportoRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return NewAeroportoInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewAeroportoInputDto newAeroportoDto = (NewAeroportoInputDto) target;
		
		Optional<Aeroporto> aeroportoBuscado = this.aeroportoRepository
				.findByNome(newAeroportoDto.getNome());
		
		if(aeroportoBuscado.isPresent()) {
			errors.rejectValue("nome", null, "Aeroporto de mesmo nome já existe no sistema.");
		}
	}

}
