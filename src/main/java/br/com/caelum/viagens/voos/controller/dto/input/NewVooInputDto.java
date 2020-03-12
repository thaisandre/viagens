package br.com.caelum.viagens.voos.controller.dto.input;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;
import br.com.caelum.viagens.voos.model.RotaSemVoo;
import br.com.caelum.viagens.voos.model.Voo;

public class NewVooInputDto {
	
	@NotEmpty
	@Valid
	private Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
	
	@NotNull
	private Long companhiaId;
	
	@NotNull
	private Long aeronaveId;

	public void setRotas(Set<NewRotaDoVooInputDto> rotas) {
		this.rotas = rotas;
	}

	public void setCompanhiaId(Long companhiaId) {
		this.companhiaId = companhiaId;
	}

	public void setAeronaveId(Long aeronaveId) {
		this.aeronaveId = aeronaveId;
	}
	
	public Set<NewRotaDoVooInputDto> getRotas() {
		return rotas;
	}
	
	public Long getCompanhiaId() {
		return companhiaId;
	}
	
	public Long getAeronaveId() {
		return aeronaveId;
	}
	
	public Voo toModel(CompanhiaRepository companhiaRepository, RotaRepository rotaRepository, AeronaveRepository aeronaveRepository) {

		Set<RotaSemVoo> rotasDoVoo = this.rotas.stream().map(r 
				-> r.toModel(rotaRepository)).collect(Collectors.toSet());
		
		Optional<Companhia> companhia = companhiaRepository.findById(companhiaId);
		Optional<Aeronave> aeronave = aeronaveRepository.findById(aeronaveId);
		
		return new Voo(rotasDoVoo, companhia.get(), aeronave.get());
	}
}
