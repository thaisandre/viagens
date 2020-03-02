package br.com.caelum.viagens.voos.controller.dto.input;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.model.Rota;
import br.com.caelum.viagens.voos.model.Voo;

public class NewVooInputDto {
	
	@NotEmpty
	@NotNull
	@Valid
	private Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
	
	@NotNull
	private Long companhiaId;
	
	@Positive
	@NotNull
	private Integer lugaresDisponiveis;

	public void setRotas(Set<NewRotaDoVooInputDto> rotas) {
		this.rotas = rotas;
	}

	public void setCompanhiaId(Long companhiaId) {
		this.companhiaId = companhiaId;
	}

	public void setLugaresDisponiveis(Integer lugaresDisponiveis) {
		this.lugaresDisponiveis = lugaresDisponiveis;
	}
	
	public Set<NewRotaDoVooInputDto> getRotas() {
		return rotas;
	}
	
	public Long getCompanhiaId() {
		return companhiaId;
	}
	
	public Integer getLugaresDisponiveis() {
		return lugaresDisponiveis;
	}
	
	public Voo toModel(CompanhiaRepository companhiaRepository, RotaRepository rotaRepository) {

		Set<Rota> rotasDoVoo = this.rotas.stream().map(r 
				-> r.toModel(rotaRepository)).collect(Collectors.toSet());
		
		Optional<Companhia> companhia = companhiaRepository.findById(companhiaId);

		return new Voo(rotasDoVoo, companhia.get(), this.lugaresDisponiveis);
	}
}
