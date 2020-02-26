package br.com.caelum.viagens.voos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.VooRepository;

@RequestMapping("/voos")
@RestController
public class VoosController {
	
	@Autowired
	private CompanhiaRepository companhiaRepository;
	
	@Autowired
	private RotaRepository rotaRepository;

	@Autowired
	private VooRepository vooRepository;
	
	@PostMapping
	public ResponseEntity<?> cadastro(@RequestBody NewVooInputDto newVooDto) {
		Voo voo = newVooDto.toModel(this.companhiaRepository, this.rotaRepository);
		this.vooRepository.save(voo);
		return ResponseEntity.ok().build();
	}
}
