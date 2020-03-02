package br.com.caelum.viagens.voos.validator;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaRepetidaValidator implements Validator{
	
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
		
		Set<Optional<Rota>> conjRotas = newVooDto.getRotas().stream()
				.map(r -> rotaRepository.findById(r.getRotaId()))
				.collect(Collectors.toSet());
		
		if(conjRotas.size() != newVooDto.getRotas().size()) {
			errors.rejectValue("rotas", null, "não é permitido repetir rotas em um voo.");
		}
	}

}
