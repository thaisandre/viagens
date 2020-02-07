package br.com.caelum.viagens.administrativo.controller.dto.input;

import javax.validation.constraints.NotBlank;

import br.com.caelum.viagens.administrativo.model.Pais;

public class NewPaisInputDto {

	@NotBlank
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Pais toModel() {
		return new Pais(this.nome);
	}

}
