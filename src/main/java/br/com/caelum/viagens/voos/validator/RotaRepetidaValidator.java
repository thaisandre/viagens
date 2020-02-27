package br.com.caelum.viagens.voos.validator;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaRepetidaValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return NewVooInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewVooInputDto newVooDto = (NewVooInputDto) target;
		
		Set<Long> conjuntoRotasId = 
				newVooDto.getRotas().stream().map(r -> r.getRotaId()).collect(Collectors.toSet());
		
		if(conjuntoRotasId.size() != newVooDto.getRotas().size()) {
			errors.reject(null, "Não é permitido repetir rotas em um voo.");
		}
	}

}
