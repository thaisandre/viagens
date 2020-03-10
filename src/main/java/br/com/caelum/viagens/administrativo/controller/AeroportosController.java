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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.AeroportoOutputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.validator.NomeAeroportoExistenteValidator;
import br.com.caelum.viagens.administrativo.validator.PaisNaoExistenteValidator;
import br.com.caelum.viagens.support.IfResourceIsFound;
import io.github.asouza.FormFlow;

@RestController
@RequestMapping("/aeroportos")
public class AeroportosController {

	@Autowired
	public PaisRepository paisRepository;
	
	@Autowired
	public AeroportoRepository aeroportoRepository;
	
	@Autowired
	private FormFlow<Aeroporto> flow;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(
				new NomeAeroportoExistenteValidator(aeroportoRepository),
				new PaisNaoExistenteValidator(paisRepository));
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> cadastro(@Valid @RequestBody NewAeroportoInputDto newAeroportoDto,
			UriComponentsBuilder uribuilder) {
		
		Aeroporto aeroporto = flow.save(newAeroportoDto).getEntity();

		URI location = uribuilder.path("/aeroportos/{id}").buildAndExpand(aeroporto.getId()).toUri();

		return ResponseEntity.created(location).body(AeroportoOutputDto.criado(aeroporto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> detalhes(@PathVariable("id") Optional<Aeroporto> aeroporto) {
		return  ResponseEntity.ok(AeroportoOutputDto.detalhes(IfResourceIsFound.of(aeroporto)));
	}

}
