package br.com.caelum.viagens.voos.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.support.Route;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;
import br.com.caelum.viagens.voos.utils.GrafoRotasUtils;

public class RotasComSequenciaLogicaValidator implements Validator {

	private RotaRepository rotaRepository;

	public RotasComSequenciaLogicaValidator(RotaRepository rotaRepository) {
		this.rotaRepository = rotaRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NewVooInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewVooInputDto newVooDto = (NewVooInputDto) target;

		if (!newVooDto.getRotas().isEmpty()) {

			List<Route> rotas = newVooDto.getRotas().stream()
					.map(r -> rotaRepository.findById(r.getRotaId()))
					.filter(r -> r.isPresent())
					.map(r -> r.get())
					.collect(Collectors.toList());
			
			if(!rotas.isEmpty()) {
				if (!GrafoRotasUtils.temSequenciaLogica(rotas)) {
					errors.rejectValue("rotas", null, "as rotas não possuem uma sequência lógica.");
				}
			}
		}

	}

}