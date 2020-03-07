package br.com.caelum.viagens.voos.validator;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotasComUmaUnicaPernaFinalValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return NewVooInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewVooInputDto newVooDto = (NewVooInputDto) target;

		if (!errors.hasErrors()) {

			Set<NewRotaDoVooInputDto> rotas = newVooDto.getRotas().stream()
					.filter(r -> r.isPernaFinal()).collect(Collectors.toSet());

			if (rotas.size() > 1) {
				errors.rejectValue("rotas", null, "a lista de rotas precisa conter apenas uma única rota final (sem parada).");
			}
			
			if(rotas.size() < 1) {
				errors.rejectValue("rotas", null, "a lista de rotas precisa conter pelo menos uma rota final (sem parada).");
			}
		}

	} 
}
