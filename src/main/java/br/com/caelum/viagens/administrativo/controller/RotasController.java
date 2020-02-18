package br.com.caelum.viagens.administrativo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.viagens.administrativo.controller.input.dto.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;

@RequestMapping("/rotas")
@RestController
public class RotasController {
	
	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
	@PostMapping
	public ResponseEntity<?> cadastro(@RequestBody NewRotaInputDto newRotaDto){
		Rota rota = newRotaDto.toModel(this.aeroportoRepository);
		this.rotaRepository.save(rota);
		return ResponseEntity.ok().build();
	}
}
