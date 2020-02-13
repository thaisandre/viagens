package br.com.caelum.viagens.administrativo.controller.dto.output;

import java.time.Instant;

import br.com.caelum.viagens.administrativo.model.Companhia;

public class CompanhiaCriadaOutputDto {

	private Long id;
	
	private String nome;
	
	private Instant instanteCriacao;
	
	private PaisCriadoOutputDto pais;

	public CompanhiaCriadaOutputDto(Companhia companhia) {
		this.id = companhia.getId();
		this.nome = companhia.getNome();
		this.instanteCriacao = companhia.getInstanteCriacao();
		this.pais = new PaisCriadoOutputDto(companhia.getPais());		
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

	public PaisCriadoOutputDto getPais() {
		return pais;
	}

}
