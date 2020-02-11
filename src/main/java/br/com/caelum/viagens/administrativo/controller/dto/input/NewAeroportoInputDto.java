package br.com.caelum.viagens.administrativo.controller.dto.input;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class NewAeroportoInputDto {

	@NotBlank
	private String nome;

	@NotNull
	private Long paisId;

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPaisId(Long paisId) {
		this.paisId = paisId;
	}

	public Aeroporto toModel(PaisRepository paisRepository) {
		Optional<Pais> pais = paisRepository.findById(this.paisId);
		return new Aeroporto(this.nome, pais.get());
	}

}
