package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;

public class AeroportosOrigemEDestinoNaoExistentesValidator implements Validator{
	
	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	public AeroportosOrigemEDestinoNaoExistentesValidator(AeroportoRepository aeroportoRepository) {
		this.aeroportoRepository = aeroportoRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return NewRotaInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewRotaInputDto newRotaDto = (NewRotaInputDto) target;
		
		if(newRotaDto.getOrigemId() != null) {
			Optional<Aeroporto> origemBuscada = this.aeroportoRepository.findById(newRotaDto.getOrigemId());
			if(!origemBuscada.isPresent()) {
				errors.rejectValue("origemId", null, "Aeroporto origem não existe no sistema.");
			}
		}
		
		if(newRotaDto.getDestinoId() != null) {
			Optional<Aeroporto> origemBuscada = this.aeroportoRepository.findById(newRotaDto.getDestinoId());
			if(!origemBuscada.isPresent()) {
				errors.rejectValue("destinoId", null, "Aeroporto destino não existe no sistema.");
			}
		}
		
	}

}
