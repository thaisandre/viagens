package br.com.caelum.viagens.voos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.util.Assert;


@Entity
public class Passagem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne
	private Voo voo;
	
	@Future
	@NotNull
	private LocalDateTime dataEHoraDePartida;
	
	@Positive
	@NotNull
	private BigDecimal valor;
	
	@Deprecated
	public Passagem() {}

	public Passagem(@NotNull Voo voo, @Future @NotNull LocalDateTime dataEHoraDePartida,
			@Positive @NotNull BigDecimal valor) {
		Assert.notNull(voo, "voo não pode ser nulo.");
		Assert.notNull(dataEHoraDePartida, "dataEHoraDePartida não pode ser nula.");
		Assert.isTrue(dataEHoraDePartida.isAfter(LocalDateTime.now()), "dataEHoraDePartida deve ser no futuro.");
		Assert.notNull(valor, "valor não pode ser nulo.");
		Assert.isTrue(valor.compareTo(BigDecimal.ZERO) > 0, "valor deve ser positivo.");
		this.voo = voo;
		this.dataEHoraDePartida = dataEHoraDePartida;
		this.valor = valor;
	}
	
	public Long getId() {
		return id;
	}
	
	public Voo getVoo() {
		return voo;
	}
	
	public LocalDateTime getDataEHoraDePartida() {
		return dataEHoraDePartida;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
}
