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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewPaisInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.output.PaisOutputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.validator.NewPaisValidator;

@RestController
@RequestMapping("/paises")
public class PaisController {
	
	@Autowired
	private PaisRepository paisRepository;
	
	@InitBinder("newPaisInputDto")
	public void InitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(new NewPaisValidator(this.paisRepository));
	}
	
	@PostMapping
	public ResponseEntity<?> cadastro(@Valid @RequestBody NewPaisInputDto newPais){
		Pais pais = newPais.toModel();
		this.paisRepository.save(pais);
		return ResponseEntity.ok(new PaisOutputDto(pais));
	}
}
