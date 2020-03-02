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

import br.com.caelum.viagens.support.Route;

@Entity
public class Rota implements Route {

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

	@Override
	public String toString() {
		return this.nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		result = prime * result + ((duracao == null) ? 0 : duracao.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((origem == null) ? 0 : origem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rota other = (Rota) obj;
		if (destino == null) {
			if (other.destino != null)
				return false;
		} else if (!destino.equals(other.destino))
			return false;
		if (duracao == null) {
			if (other.duracao != null)
				return false;
		} else if (!duracao.equals(other.duracao))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (origem == null) {
			if (other.origem != null)
				return false;
		} else if (!origem.equals(other.origem))
			return false;
		return true;
	}

}
