package br.com.caelum.viagens.voos.controller.dto.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import br.com.caelum.viagens.voos.model.Passagem;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.VooRepository;


public class NewPassagemInputDto {
	
	@NotNull
	private Long vooId;
	
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	@Future
	@NotNull
	private LocalDateTime dataEHoraDePartida;
	
	@JsonFormat(shape= Shape.NUMBER_FLOAT)
	@Positive
	@NotNull
	private BigDecimal valor;
	
	public Long getVooId() {
		return vooId;
	}
	
	public void setVooId(Long vooId) {
		this.vooId = vooId;
	}

	public void setDataEHoraDePartida(LocalDateTime dataEHoraDePartida) {
		this.dataEHoraDePartida = dataEHoraDePartida;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Passagem toModel(VooRepository vooRepository) {
		Optional<Voo> voo = vooRepository.findById(this.vooId);
		return new Passagem(voo.get(), this.dataEHoraDePartida, this.valor);
	}
	
}
