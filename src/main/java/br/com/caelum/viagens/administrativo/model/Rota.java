package br.com.caelum.viagens.administrativo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.util.Assert;

@Entity
public class Rota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;

	@NotNull
	@ManyToOne
	private Aeroporto origem;

	@NotNull
	@ManyToOne
	private Aeroporto destino;

	@Positive
	@NotNull
	private Integer duracao;

	@Deprecated
	public Rota() {
	}

	public Rota(@NotNull Aeroporto origem, @NotNull Aeroporto destino, @Positive @NotNull Integer duracao) {
		this(origem.getNome() + "-" + destino.getNome(), origem, destino, duracao);
	}

	public Rota(@NotBlank String nome, @NotNull Aeroporto origem, @NotNull Aeroporto destino,
			@NotNull @Positive Integer duracao) {		
		Assert.hasText(nome, "nome não pode ser vazio.");
		Assert.notNull(origem, "origem não pode ser nulo.");
		Assert.notNull(destino, "destino não pode ser nulo.");
		Assert.notNull(duracao, "duracao não pode ser nulo.");
		Assert.isTrue(duracao > 0, () -> "duracao deve ser positivo.");
		Assert.isTrue(!origem.equals(destino), () -> "origem deve ser diferente do destino.");
		this.nome = nome;
		this.origem = origem;
		this.destino = destino;
		this.duracao = duracao;
	}

	public Long getId() {
		return this.id;
	}

	public String getNome() {
		return nome;
	}

	public Aeroporto getOrigem() {
		return origem;
	}

	public Aeroporto getDestino() {
		return destino;
	}

	public Integer getDuracao() {
		return duracao;
	}
}
