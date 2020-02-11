package br.com.caelum.viagens.administrativo.controller.dto.output;

import br.com.caelum.viagens.administrativo.model.Aeroporto;

public class AeroportoOutputDto {

	private Long id;

	private String nome;

	private PaisOutputDto pais;
	
	public AeroportoOutputDto(Aeroporto aeroporto) {
		this.id = aeroporto.getId();
		this.nome = aeroporto.getNome();
		this.pais = new PaisOutputDto(aeroporto.getPais());
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public PaisOutputDto getPais() {
		return pais;
	}

}
