package br.com.caelum.viagens.voos.controller.dto.input;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.model.Rota;

public class NewRotaDoVooInputDto {

	@NotNull
	private Long rotaId;
	
	@Valid
	private NewParadaDaRotaInputDto parada;

	public void setRotaId(Long rotaId) {
		this.rotaId = rotaId;
	}

	public void setParada(NewParadaDaRotaInputDto parada) {
		this.parada = parada;
	}
	
	public Long getRotaId() {
		return rotaId;
	}

	public NewParadaDaRotaInputDto getParada() {
		return this.parada;
	}
	
	public boolean isPernaFinal() {
		return parada == null;
	}
	
	public Rota toModel(RotaRepository rotaRepository) {
		Optional<br.com.caelum.viagens.administrativo.model.Rota> rota = rotaRepository.findById(rotaId);
		Rota rotaVoo = new Rota(rota.get());

		if (parada != null) {
			rotaVoo.setParada(parada.ToModel());
		}
		return rotaVoo;
	}
	
}
