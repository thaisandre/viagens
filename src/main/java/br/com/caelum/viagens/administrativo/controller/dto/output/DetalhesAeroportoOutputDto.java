package br.com.caelum.viagens.administrativo.controller.dto.output;

import br.com.caelum.viagens.administrativo.model.Aeroporto;

public class DetalhesAeroportoOutputDto {
	
	private Long id;

	private String nome;

	private PaisCriadoOutputDto pais;
	
	public DetalhesAeroportoOutputDto(Aeroporto aeroporto) {
		this.id = aeroporto.getId();
		this.nome = aeroporto.getNome();
		this.pais = new PaisCriadoOutputDto(aeroporto.getPais());
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public PaisCriadoOutputDto getPais() {
		return pais;
	}
}
