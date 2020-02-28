package br.com.caelum.viagens.voos.controller.dto.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
	private List<NewRotaDoVooInputDto> rotas = new ArrayList<NewRotaDoVooInputDto>();
	
	@NotNull
	private Long companhiaId;
	
	@Positive
	@NotNull
	private Integer lugaresDisponiveis;

	public void setRotas(List<NewRotaDoVooInputDto> rotas) {
		this.rotas = rotas;
	}

	public void setCompanhiaId(Long companhiaId) {
		this.companhiaId = companhiaId;
	}

	public void setLugaresDisponiveis(Integer lugaresDisponiveis) {
		this.lugaresDisponiveis = lugaresDisponiveis;
	}
	
	public List<NewRotaDoVooInputDto> getRotas() {
		return rotas;
	}
	
	public Long getCompanhiaId() {
		return companhiaId;
	}
	
	public Integer getLugaresDisponiveis() {
		return lugaresDisponiveis;
	}
	
	public Voo toModel(CompanhiaRepository companhiaRepository, RotaRepository rotaRepository) {

		List<Rota> rotasDoVoo = this.rotas.stream().map(r 
				-> r.toModel(rotaRepository)).collect(Collectors.toList());
		
		Optional<Companhia> companhia = companhiaRepository.findById(companhiaId);

		return new Voo(rotasDoVoo, companhia.get(), this.lugaresDisponiveis);
	}
}
