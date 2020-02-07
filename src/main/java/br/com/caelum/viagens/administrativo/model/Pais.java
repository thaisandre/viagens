package br.com.caelum.viagens.administrativo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Pais {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nome;

	@Deprecated
	public Pais() {}

	public Pais(@NotBlank String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}
}
