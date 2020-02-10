package br.com.caelum.viagens.administrativo.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

@Entity
public class Companhia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm:ss")
	@NotNull
	private Instant instanteCriacao = Instant.now();

	@ManyToOne
	@NotNull
	private Pais pais;

	@Deprecated
	public Companhia() {
	}

	public Companhia(@NotBlank String nome, @NotNull Pais pais) {
		this.nome = nome;
		this.pais = pais;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Instant getInstanteCriacao() {
		return instanteCriacao;
	}

	public Pais getPais() {
		return pais;
	}

}
