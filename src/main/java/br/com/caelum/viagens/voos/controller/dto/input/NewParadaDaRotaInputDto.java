package br.com.caelum.viagens.voos.controller.dto.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.caelum.viagens.voos.model.Parada;
import br.com.caelum.viagens.voos.model.TipoParada;
import br.com.caelum.viagens.voos.validator.annotations.TipoParadaExistente;

public class NewParadaDaRotaInputDto {
	
	@TipoParadaExistente
	@NotNull
	private String tipo;

	@NotNull
	@Positive
	private Integer tempo;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

	public String getTipo() {
		return this.tipo;
	}
	
	public Integer getTempo() {
		return tempo;
	}
	
	public Parada ToModel() {
		return new Parada(tempo, TipoParada.valueOf(tipo));
	}

}
