package br.com.caelum.viagens.aeronaves.controller.dto.input;

import javax.validation.constraints.NotNull;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Modelo;

public class NewAeronaveInputDto {
	
	@NotNull
	private Modelo modelo;

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}
	
	public Modelo getModelo() {
		return modelo;
	}

	public Aeronave toModel() {
		return new Aeronave(this.modelo);
	}
}
