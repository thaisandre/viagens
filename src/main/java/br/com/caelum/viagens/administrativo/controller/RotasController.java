package br.com.caelum.viagens.administrativo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.administrativo.validator.AeroportoOrigemEDestinoNaoExistentesValidator;
import br.com.caelum.viagens.administrativo.validator.AeroportosOrigemEDestinoDiferentesValidator;

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
				new AeroportoOrigemEDestinoNaoExistentesValidator(aeroportoRepository),
				new AeroportosOrigemEDestinoDiferentesValidator());
	}
	
	@PostMapping
	public ResponseEntity<?> cadastro(@Valid @RequestBody NewRotaInputDto newRotaDto){
		Rota rota = newRotaDto.toModel(this.aeroportoRepository);
		this.rotaRepository.save(rota);
		return ResponseEntity.ok().build();
	}
}
