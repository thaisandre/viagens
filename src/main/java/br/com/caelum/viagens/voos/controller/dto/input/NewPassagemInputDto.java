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
	private Long voo_id;
	
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	@Future
	@NotNull
	private LocalDateTime dataEHoraDePartida;
	
	@Positive
	@NotNull
	private BigDecimal valor;

	public void setVoo_id(Long voo_id) {
		this.voo_id = voo_id;
	}

	public void setDataEHoraDePartida(LocalDateTime dataEHoraDePartida) {
		this.dataEHoraDePartida = dataEHoraDePartida;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Passagem toModel(VooRepository vooRepository) {
		Optional<Voo> voo = vooRepository.findById(this.voo_id);
		return new Passagem(voo.get(), this.dataEHoraDePartida, this.valor);
	}
	
	
}
