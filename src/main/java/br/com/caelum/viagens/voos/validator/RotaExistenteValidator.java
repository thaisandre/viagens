package br.com.caelum.viagens.voos.validator;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaExistenteValidator implements Validator {

	private RotaRepository rotaRepository;

	public RotaExistenteValidator(RotaRepository rotaRepository) {
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
			Set<Long> idsDto = newVooDto.getRotas().stream()
					.map(r -> r.getRotaId()).collect(Collectors.toSet());

			Set<Long> rotasIds = ((Collection<Rota>) rotaRepository.findAllById(idsDto))
					.stream().map(r -> r.getId()).collect(Collectors.toSet());

			idsDto.forEach(id -> {
				if(!rotasIds.contains(id)) {
					errors.rejectValue("rotas", null, "rotaId " + id + " não existe no sistema.");
				}
			});
		}
	}

}
