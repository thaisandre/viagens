package br.com.caelum.viagens.administrativo.controller.dto.input;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.util.StringUtils;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;

public class NewRotaInputDto {

	private String nome;

	@NotNull
	private Long origemId;

	@NotNull
	private Long destinoId;

	@Positive
	@NotNull
	private Integer duracao;

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setOrigemId(Long origemId) {
		this.origemId = origemId;
	}

	public void setDestinoId(Long destinoId) {
		this.destinoId = destinoId;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	public Long getOrigemId() {
		return this.origemId;
	}

	public Long getDestinoId() {
		return this.destinoId;
	}
	
	public Integer getDuracao() {
		return duracao;
	}

	public Rota toModel(AeroportoRepository aeroportoRepository) {
		Optional<Aeroporto> aeroportoOrigem = aeroportoRepository.findById(this.origemId);
		Optional<Aeroporto> aeroportoDestino = aeroportoRepository.findById(this.destinoId);
		
		if(StringUtils.hasText(this.nome)) {
			return new Rota(this.nome, aeroportoOrigem.get(), aeroportoDestino.get(), this.duracao);
		} else {
			return new Rota(aeroportoOrigem.get(), aeroportoDestino.get(), this.duracao);
		}
	}
}