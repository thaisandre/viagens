package br.com.caelum.viagens.voos.controller;

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

import br.com.caelum.viagens.aeronaves.repository.AssentoRepository;
import br.com.caelum.viagens.support.IfResourceIsFound;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;
import br.com.caelum.viagens.voos.controller.dto.output.PassagemOutputDto;
import br.com.caelum.viagens.voos.model.Passagem;
import br.com.caelum.viagens.voos.repository.PassagemRepository;
import br.com.caelum.viagens.voos.repository.VooRepository;
import br.com.caelum.viagens.voos.validator.AssentoDisponivelValidator;
import br.com.caelum.viagens.voos.validator.AssentoExistenteNoVooValidator;
import br.com.caelum.viagens.voos.validator.AssentoExistenteValidator;
import br.com.caelum.viagens.voos.validator.VooExistenteValidator;
import io.github.asouza.FormFlow;

@RequestMapping("/passagens")
@RestController
public class PassagensController {
	
	@Autowired
	private VooRepository vooRepository;
	
	@Autowired
	private FormFlow<Passagem> flow;

	@Autowired
	private PassagemRepository passagemRepository;

	@Autowired
	private AssentoRepository assentoRepository;
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(new VooExistenteValidator(vooRepository),
				new AssentoExistenteValidator(assentoRepository),
				new AssentoExistenteNoVooValidator(vooRepository),
				new AssentoDisponivelValidator(passagemRepository));
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> cadastro(@Valid @RequestBody NewPassagemInputDto newPassagemDto,
			UriComponentsBuilder uriBuilder) {
		Passagem passagem = flow.save(newPassagemDto).getEntity();
		
		URI location = uriBuilder.path("/passagens/{id}").buildAndExpand(passagem.getId()).toUri();
		
		return ResponseEntity.created(location).body(PassagemOutputDto.criado(passagem));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> detalhes(@PathVariable("id") Optional<Passagem> passagem){
		return ResponseEntity.ok(PassagemOutputDto.detalhes(IfResourceIsFound.of(passagem)));
	}
}
