package br.com.caelum.viagens.administrativo.controller.dto.output;

import java.time.Instant;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;

public class CompanhiaOutputDto {

	private Long id;
	
	private String nome;
	
	private Instant instanteCriacao;
	
	private Pais pais;

	public CompanhiaOutputDto(Companhia companhia) {
		this.id = companhia.getId();
		this.nome = companhia.getNome();
		this.instanteCriacao = companhia.getInstanteCriacao();
		this.pais = companhia.getPais();		
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
