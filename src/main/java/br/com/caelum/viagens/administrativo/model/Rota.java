package br.com.caelum.viagens.administrativo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.springframework.util.Assert;

import com.sun.istack.NotNull;

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

	public Rota(@NotBlank String nome, @NotNull Aeroporto origem, @NotNull Aeroporto destino,
			@NotNull @Positive Integer duracao) {
		Assert.hasText(nome, "nome n]ao pode ser vazio.");
		Assert.notNull(origem, "origem não pode ser nulo.");
		Assert.notNull(destino, "destino não pode ser nulo.");
		Assert.notNull(duracao, "duracao não pode ser nulo.");
		Assert.isTrue(duracao > 0, () -> "duracao deve ser positivo");
		this.nome = nome;
		this.origem = origem;
		this.destino = destino;
		this.duracao = duracao;
	}
}
