package br.com.caelum.viagens.voos.validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

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
		
		if(!newVooDto.getRotas().isEmpty()) {
			
			if(!temSequenciaLogica(newVooDto.getRotas())) {
				errors.reject(null, "As rotas não possuem uma sequência lógica.");
			}
		}

	}

	private boolean temSequenciaLogica(List<NewRotaDoVooInputDto> rotasDoVoo) {

		List<Optional<br.com.caelum.viagens.administrativo.model.Rota>> rotas = rotasDoVoo.stream()
				.map(r -> rotaRepository.findById(r.getRotaId())).collect(Collectors.toList());
		
		Optional<br.com.caelum.viagens.administrativo.model.Rota> anterior = rotas.get(0);
		for (int i = 1; i < rotas.size(); i++) {
			if (anterior.isPresent() && rotas.get(i).isPresent()) {
				if (!anterior.get().getDestino().equals(rotas.get(i).get().getOrigem())) {
					return false;
				}
				anterior = rotas.get(i);
			}
		}
		return true;
	}

}
