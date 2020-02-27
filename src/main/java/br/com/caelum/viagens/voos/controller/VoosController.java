package br.com.caelum.viagens.voos.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.VooRepository;
import br.com.caelum.viagens.voos.validator.CompanhiaExistenteValidator;
import br.com.caelum.viagens.voos.validator.RotaComTipoDeParadaExistenteValidator;
import br.com.caelum.viagens.voos.validator.RotaExistenteValidator;
import br.com.caelum.viagens.voos.validator.RotaRepetidaValidator;
import br.com.caelum.viagens.voos.validator.RotasComSequenciaLogicaValidator;
import br.com.caelum.viagens.voos.validator.RotasComUmaUnicaPernaFinalValidator;

@RequestMapping("/voos")
@RestController
public class VoosController {

	@Autowired
	private CompanhiaRepository companhiaRepository;

	@Autowired
	private RotaRepository rotaRepository;

	@Autowired
	private VooRepository vooRepository;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(
				new CompanhiaExistenteValidator(this.companhiaRepository),
				new RotaExistenteValidator(rotaRepository),
				new RotaComTipoDeParadaExistenteValidator(),
				new RotasComUmaUnicaPernaFinalValidator(),
				new RotaRepetidaValidator(),
				new RotasComSequenciaLogicaValidator(rotaRepository));
	}

	@PostMapping
	public ResponseEntity<?> cadastro(@Valid @RequestBody NewVooInputDto newVooDto) {
		Voo voo = newVooDto.toModel(this.companhiaRepository, this.rotaRepository);
		this.vooRepository.save(voo);
		return ResponseEntity.ok().build();
	}
}
