package br.com.caelum.viagens.voos.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.util.Assert;

public class Parada {

	@NotNull
	@Positive
	private Integer tempo;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoParada tipo;

	@Deprecated
	public Parada() {
	}

	public Parada(@NotNull @Positive Integer tempo, @NotNull TipoParada tipo) {
		Assert.notNull(tempo, "Tempo não pode ser nulo.");
		Assert.isTrue(tempo > 0, () -> "Tempo deve ser positivo.");
		Assert.notNull(tipo, "Tipo de parada não pode ser nulo.");
		this.tempo = tempo;
		this.tipo = tipo;
	}
	
	public Integer getTempo() {
		return tempo;
	}
	
	public TipoParada getTipo() {
		return tipo;
	}

}
