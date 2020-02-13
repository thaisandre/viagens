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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.CompanhiaCriadaOutputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.DetalhesCompanhiaOutputDto;
import br.com.caelum.viagens.administrativo.exception.ResourceNotFoundException;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.validator.NomeCompanhiaExistenteValidator;

@RestController
@RequestMapping("/companhias")
public class CompanhiasController {

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private CompanhiaRepository companhiaRepository;

	@InitBinder()
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(
				new NomeCompanhiaExistenteValidator(this.companhiaRepository));
		}

	@PostMapping
	public ResponseEntity<CompanhiaCriadaOutputDto> cadastro(@Valid @RequestBody NewCompanhiaInputDto newCompanhiaDto,
			UriComponentsBuilder uriBuilder) {
		Companhia companhia = newCompanhiaDto.toModel(this.paisRepository);
		this.companhiaRepository.save(companhia);
		
		URI location = uriBuilder.path("/companhias/{id}")
				.buildAndExpand(companhia.getId()).toUri();
		
		return ResponseEntity.created(location).body(new CompanhiaCriadaOutputDto(companhia));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesCompanhiaOutputDto> detalhes(@PathVariable("id") Optional<Companhia> companhia){
		return ResponseEntity.ok(new DetalhesCompanhiaOutputDto(companhia.orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado."))));
	}
}
