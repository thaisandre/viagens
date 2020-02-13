package br.com.caelum.viagens.administrativo.controller.dto.output;

import br.com.caelum.viagens.administrativo.model.Aeroporto;

public class AeroportoCriadoOutputDto {

	private Long id;

	private String nome;

	private PaisDoAeroportoCriadoOutputDto pais;
	
	public AeroportoCriadoOutputDto(Aeroporto aeroporto) {
		this.id = aeroporto.getId();
		this.nome = aeroporto.getNome();
		this.pais = new PaisDoAeroportoCriadoOutputDto(aeroporto.getPais());
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public PaisDoAeroportoCriadoOutputDto getPais() {
		return pais;
	}

}
