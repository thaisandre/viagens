package br.com.caelum.viagens.voos.controller;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.viagens.support.IfResourceIsFound;
import br.com.caelum.viagens.voos.controller.dto.input.UpdateInputPassagemDto;
import br.com.caelum.viagens.voos.controller.dto.output.PassagemOutputDto;
import br.com.caelum.viagens.voos.model.Passagem;
import io.github.asouza.FormFlow;

@RequestMapping("/passagens")
@RestController
public class PassagensController {
	
	@Autowired
	private FormFlow<Passagem> flow;
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> cadastro(@PathVariable("id") Optional<Passagem> passagem,
			@Valid @RequestBody UpdateInputPassagemDto updatePassagemDto) {
		
		Passagem passagemAtualizada = flow.save(updatePassagemDto, 
				IfResourceIsFound.of(passagem)).getEntity();
		
		return ResponseEntity.ok(PassagemOutputDto.atualizada(passagemAtualizada));
	}
}
