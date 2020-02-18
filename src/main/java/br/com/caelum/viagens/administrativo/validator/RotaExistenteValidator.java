package br.com.caelum.viagens.administrativo.validator;

import java.util.Optional;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;

public class RotaExistenteValidator implements Validator{
	
	private RotaRepository rotaRepository;
	private AeroportoRepository aeroportoRepository;
	
	public RotaExistenteValidator(RotaRepository rotaRepository, AeroportoRepository aeroportoRepository) {
		this.rotaRepository = rotaRepository;
		this.aeroportoRepository = aeroportoRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return NewRotaInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NewRotaInputDto newRotaDto = (NewRotaInputDto) target;
		
		if(newRotaDto.getOrigemId() != null && newRotaDto.getDestinoId() != null) {
			Optional<Aeroporto> origem = this.aeroportoRepository.findById(newRotaDto.getOrigemId());
			Optional<Aeroporto> destino = this.aeroportoRepository.findById(newRotaDto.getDestinoId());
			
			if(origem.isPresent() && destino.isPresent()) {
				Optional<Rota> rotaBuscada = this.rotaRepository.findByOrigemAndDestino(origem.get(), destino.get());
				if(rotaBuscada.isPresent()) {
					errors.reject("Unique.newRotaInputDto", null, "Rota já existe no sistema.");
				}
			}
		
		}
	}

}
