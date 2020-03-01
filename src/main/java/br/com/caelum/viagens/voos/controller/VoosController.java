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

import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.support.IfResourceIsFound;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.output.VooOutputDto;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.validator.CompanhiaExistenteValidator;
import br.com.caelum.viagens.voos.validator.RotaExistenteValidator;
import br.com.caelum.viagens.voos.validator.RotaRepetidaValidator;
import br.com.caelum.viagens.voos.validator.RotasComSequenciaLogicaValidator;
import br.com.caelum.viagens.voos.validator.RotasComUmaUnicaPernaFinalValidator;
import io.github.asouza.FormFlow;

@RequestMapping("/voos")
@RestController
public class VoosController {

	@Autowired
	private CompanhiaRepository companhiaRepository;

	@Autowired
	private RotaRepository rotaRepository;
	
	@Autowired
	private FormFlow<Voo> flow;
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(
				new CompanhiaExistenteValidator(this.companhiaRepository),
				new RotaExistenteValidator(rotaRepository),
				new RotasComUmaUnicaPernaFinalValidator(),
				new RotaRepetidaValidator(),
				new RotasComSequenciaLogicaValidator(rotaRepository));
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> cadastro(@Valid @RequestBody NewVooInputDto newVooDto,
			UriComponentsBuilder uriBuilder) {
		Voo voo = flow.save(newVooDto).getEntity();
		
		URI location = uriBuilder.path("/voos/{id}").buildAndExpand(voo.getId()).toUri();
		
		return ResponseEntity.created(location ).body(VooOutputDto.criado(voo));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> detalhes(@PathVariable("id") Optional<Voo> voo){
		return ResponseEntity.ok(VooOutputDto.detalhes(IfResourceIsFound.of(voo)));
	}
}
