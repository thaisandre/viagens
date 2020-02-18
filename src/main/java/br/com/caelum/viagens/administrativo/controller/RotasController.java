package br.com.caelum.viagens.administrativo.controller;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.RotaOutputDto;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.administrativo.support.IfResourceIsFound;
import br.com.caelum.viagens.administrativo.validator.AeroportosOrigemEDestinoDiferentesValidator;
import br.com.caelum.viagens.administrativo.validator.AeroportosOrigemEDestinoNaoExistentesValidator;
import br.com.caelum.viagens.administrativo.validator.RotaExistenteValidator;

@RequestMapping("/rotas")
@RestController
public class RotasController {
	
	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
	@InitBinder
	public void InitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(
				new AeroportosOrigemEDestinoNaoExistentesValidator(aeroportoRepository),
				new AeroportosOrigemEDestinoDiferentesValidator(),
				new RotaExistenteValidator(rotaRepository, aeroportoRepository));
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> cadastro(@Valid @RequestBody NewRotaInputDto newRotaDto,
			UriComponentsBuilder uriBuilder){
		Rota rota = newRotaDto.toModel(this.aeroportoRepository);
		this.rotaRepository.save(rota);
		
		URI location = uriBuilder.path("/rotas/{id}").buildAndExpand(rota.getId()).toUri();
		
		return ResponseEntity.created(location).body(RotaOutputDto.criado(rota));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> detalhes(@PathVariable("id") Optional<Rota> rota){
		return ResponseEntity.ok(RotaOutputDto.detalhes(IfResourceIsFound.of(rota)));
	}
}
