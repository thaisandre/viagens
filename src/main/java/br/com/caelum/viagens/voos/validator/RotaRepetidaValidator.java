package br.com.caelum.viagens.voos.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaRepetidaValidator implements Validator {

	private RotaRepository rotaRepository;

	public RotaRepetidaValidator(RotaRepository rotaRepository) {
		this.rotaRepository = rotaRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NewVooInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewVooInputDto newVooDto = (NewVooInputDto) target;
		
		if(!errors.hasErrors()) {
			List<Long> ids = newVooDto.getRotas().stream().map(r -> r.getRotaId()).collect(Collectors.toList());
	
			List<Rota> rotas = (List<Rota>) rotaRepository.findAllById(ids);
	
			if (rotas.size() != newVooDto.getRotas().size()) {
				errors.rejectValue("rotas", null, "não é permitido repetir rotas em um voo.");
			}
		}
	}

}
