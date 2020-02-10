package br.com.caelum.viagens.administrativo.controller.dto.output;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;

public class CompanhiaOutputDto {

	private Long id;
	private String nome;
	private Pais pais;

	public CompanhiaOutputDto(Companhia companhia) {
		this.id = companhia.getId();
		this.nome = companhia.getNome();
		this.pais = companhia.getPais();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Pais getPais() {
		return pais;
	}

}
