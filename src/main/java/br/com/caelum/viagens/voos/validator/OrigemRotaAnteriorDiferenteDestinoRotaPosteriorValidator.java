package br.com.caelum.viagens.voos.validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class OrigemRotaAnteriorDiferenteDestinoRotaPosteriorValidator implements Validator {
	
	private RotaRepository rotaRepository;
	
	public OrigemRotaAnteriorDiferenteDestinoRotaPosteriorValidator(RotaRepository rotaRepository) {
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
				errors.reject(null, "A origem de uma rota anterior não pode ser igual ao destino de uma rota posterior.");
			}
		}
		
	}

	private boolean temSequenciaLogica(List<NewRotaDoVooInputDto> rotasDoVoo) {

		List<Optional<br.com.caelum.viagens.administrativo.model.Rota>> rotas = rotasDoVoo.stream()
				.map(r -> rotaRepository.findById(r.getRotaId())).collect(Collectors.toList());

		for (int i = 0; i < rotas.size() - 1; i++) {
			for(int j = i+1; j < rotas.size(); j++) {
				if(rotas.get(i).isPresent()) {
					if(rotas.get(i).get().getOrigem().equals(rotas.get(j).get().getDestino())) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
