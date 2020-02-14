package br.com.caelum.viagens.administrativo.controller.dto.input;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.support.IfResourceIsFound;

public class NewAeroportoInputDto {

	@NotBlank
	private String nome;

	@NotNull
	private Long paisId;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPaisId(Long paisId) {
		this.paisId = paisId;
	}

	public Long getPaisId() {
		return paisId;
	}

	public Aeroporto toModel(PaisRepository paisRepository) {
		Optional<Pais> pais = paisRepository.findById(this.paisId);
		return new Aeroporto(this.nome, IfResourceIsFound.of(pais, "paisId não encontrado."));
	}

}
