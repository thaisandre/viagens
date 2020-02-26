package br.com.caelum.viagens.voos.controller.dto.input;

import java.util.Optional;

import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.model.Parada;
import br.com.caelum.viagens.voos.model.Rota;
import br.com.caelum.viagens.voos.model.TipoParada;

public class NewRotaDoVooInputDto {

	private Long rotaId;

	private String tipo;

	private Integer tempoParada;

	public void setRotaId(Long rotaId) {
		this.rotaId = rotaId;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setTempoParada(Integer tempoParada) {
		this.tempoParada = tempoParada;
	}
	
	public Rota toModel(RotaRepository rotaRepository) {
		Optional<br.com.caelum.viagens.administrativo.model.Rota> rota = rotaRepository.findById(rotaId);
		Parada parada = null;
		if(tempoParada != null && tipo != null) {
			parada = new Parada(tempoParada, TipoParada.valueOf(tipo));
		}
		return new Rota(rota.get(), parada);
	}

}
