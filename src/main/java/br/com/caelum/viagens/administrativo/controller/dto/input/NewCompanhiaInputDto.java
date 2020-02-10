package br.com.caelum.viagens.administrativo.controller.dto.input;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class NewCompanhiaInputDto {

	@NotBlank
	private String nome;

	@NotBlank
	private String pais;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Companhia toModel(PaisRepository paisRepository) {
		Optional<Pais> pais = paisRepository.findByNome(this.pais);
		return new Companhia(this.nome, pais.get());
	}

}
