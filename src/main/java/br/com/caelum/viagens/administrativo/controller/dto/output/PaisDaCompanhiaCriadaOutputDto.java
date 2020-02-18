package br.com.caelum.viagens.administrativo.controller.dto.output;

import br.com.caelum.viagens.administrativo.model.Pais;

public class PaisDaCompanhiaCriadaOutputDto {

	private Long id;
	private String nome;

	public PaisDaCompanhiaCriadaOutputDto(Pais pais) {
		this.id = pais.getId();
		this.nome = pais.getNome();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
}
