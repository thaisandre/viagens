package br.com.caelum.viagens.administrativo.controller.dto.output;

import java.time.Instant;

import br.com.caelum.viagens.administrativo.model.Companhia;

public class DetalhesCompanhiaOutputDto {
	
private Long id;
	
	private String nome;
	
	private Instant instanteCriacao;
	
	private DetalhesPaisOutputDto pais;

	public DetalhesCompanhiaOutputDto(Companhia companhia) {
		this.id = companhia.getId();
		this.nome = companhia.getNome();
		this.instanteCriacao = companhia.getInstanteCriacao();
		this.pais = new DetalhesPaisOutputDto(companhia.getPais());		
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

	public DetalhesPaisOutputDto getPais() {
		return pais;
	}
}