package br.com.caelum.viagens.voos.validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
		System.out.println("chamou rota existente");
		
		if (newVooDto.getRotas() != null) {
			List<Optional<br.com.caelum.viagens.administrativo.model.Rota>> rotas = newVooDto.getRotas().stream()
					.map(r -> rotaRepository.findById(r.getRotaId())).collect(Collectors.toList());

			rotas.forEach(r -> {
				if (!r.isPresent()) {
					errors.rejectValue("rotas", null, "rotaId não existe no sistema.");
				}
			});
		}
	}

}
