package br.com.caelum.viagens.voos.controller.dto.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.caelum.viagens.voos.model.Parada;
import br.com.caelum.viagens.voos.model.TipoParada;

public class NewParadaDaRotaInputDto {

	@NotNull
	private TipoParada tipo;

	@NotNull
	@Positive
	private Integer tempo;

	public void setTipo(TipoParada tipo) {
		this.tipo = tipo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

	
	public TipoParada getTipo() { 
		return this.tipo; 
	}

	public Integer getTempo() {
		return tempo;
	}

	public Parada ToModel() {
		return new Parada(tempo, tipo);
	}

}
