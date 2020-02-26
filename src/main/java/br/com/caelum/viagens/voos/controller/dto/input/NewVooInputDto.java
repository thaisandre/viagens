package br.com.caelum.viagens.voos.controller.dto.input;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.model.Rota;
import br.com.caelum.viagens.voos.model.Voo;

public class NewVooInputDto {

	private List<NewRotaDoVooInputDto> rotas;

	private Long companhiaId;

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

	public Voo toModel(CompanhiaRepository companhiaRepository, RotaRepository rotaRepository) {

		Set<Rota> rotasDoVoo = this.rotas.stream().map(r 
				-> r.toModel(rotaRepository)).collect(Collectors.toSet());

		Optional<Companhia> companhia = companhiaRepository.findById(companhiaId);

		return new Voo(rotasDoVoo, companhia.get(), this.lugaresDisponiveis);
	}
}
