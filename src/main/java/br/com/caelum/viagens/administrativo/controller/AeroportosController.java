package br.com.caelum.viagens.administrativo.controller;

import java.net.URI;
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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.AeroportoCriadoOutputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.DetalhesAeroportoOutputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.validator.NomeAeroportoExistenteValidator;
import br.com.caelum.viagens.administrativo.validator.PaisNaoExistenteValidator;

@RestController
@RequestMapping("/aeroportos")
public class AeroportosController {
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	@InitBinder("newAeroportoInputDto")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(
				new NomeAeroportoExistenteValidator(aeroportoRepository),
				new PaisNaoExistenteValidator(paisRepository));
	}

	@PostMapping
	public ResponseEntity<AeroportoCriadoOutputDto> cadastro(@Valid @RequestBody NewAeroportoInputDto newAeroportoDto,
			UriComponentsBuilder uribuilder){
		Aeroporto aeroporto = newAeroportoDto.toModel(this.paisRepository);
		this.aeroportoRepository.save(aeroporto);
		
		URI location = uribuilder.path("/aeroportos/{id}")
				.buildAndExpand(aeroporto.getId()).toUri();
		
		return ResponseEntity.created(location).body(new AeroportoCriadoOutputDto(aeroporto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesAeroportoOutputDto> detalhes(@PathVariable("id") Optional<Aeroporto> aeroporto) {
		if(!aeroporto.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(new DetalhesAeroportoOutputDto(aeroporto.get()));
	}
}
