package br.com.caelum.viagens.voos.controller.dto.input;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.caelum.viagens.voos.model.Passagem;

public class UpdateInputPassagemDto {

	@JsonFormat(shape = Shape.NUMBER_FLOAT)
	@Positive
	@NotNull
	private BigDecimal valor;

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Passagem toModel(Passagem passagem) {
		passagem.setValor(this.valor);
		return passagem;
	}

}
