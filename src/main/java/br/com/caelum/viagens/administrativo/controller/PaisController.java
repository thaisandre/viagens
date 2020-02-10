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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewPaisInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.PaisOutputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.validator.PaisExistenteValidator;

@RestController
@RequestMapping("/paises")
public class PaisController {

	@Autowired
	private PaisRepository paisRepository;

	@InitBinder("newPaisInputDto")
	public void InitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(new PaisExistenteValidator(this.paisRepository));
	}

	@PostMapping
	public ResponseEntity<PaisOutputDto> cadastro(@Valid @RequestBody NewPaisInputDto newPais, UriComponentsBuilder uriBuilder) {
		Pais pais = newPais.toModel();
		this.paisRepository.save(pais);

		URI location = uriBuilder.path("/paises/{id}")
				.buildAndExpand(pais.getId()).toUri();

		return ResponseEntity.created(location).body(new PaisOutputDto(pais));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PaisOutputDto> detalhesPais(@PathVariable("id") Optional<Pais> pais){
		if(!pais.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(new PaisOutputDto(pais.get()));
		
	}
}
