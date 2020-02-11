package br.com.caelum.viagens.administrativo.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.AeroportoOutputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@RestController
@RequestMapping("/aeroportos")
public class AeroportosController {
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private AeroportoRepository aeroportoRepository;

	@PostMapping
	public ResponseEntity<AeroportoOutputDto> cadastro(@RequestBody NewAeroportoInputDto newAeroportoDto,
			UriComponentsBuilder uribuilder){
		Aeroporto aeroporto = newAeroportoDto.toModel(this.paisRepository);
		this.aeroportoRepository.save(aeroporto);
		
		URI location = uribuilder.path("/aeroportos/{id}")
				.buildAndExpand(aeroporto.getId()).toUri();
		
		return ResponseEntity.created(location).body(new AeroportoOutputDto(aeroporto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AeroportoOutputDto> detalhes(@PathVariable("id") Optional<Aeroporto> aeroporto) {
		if(!aeroporto.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(new AeroportoOutputDto(aeroporto.get()));
	}
}
