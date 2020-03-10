package br.com.caelum.viagens.voos.controller.dto.input;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.model.RotaSemVoo;

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
	
	public RotaSemVoo toModel(RotaRepository rotaRepository) {
		Optional<br.com.caelum.viagens.administrativo.model.Rota> rota = rotaRepository.findById(rotaId);
		RotaSemVoo rotaDoVoo = new RotaSemVoo(rota.get());

		if (parada != null) {
			rotaDoVoo.setParada(parada.ToModel());
		}
		return rotaDoVoo;
	}
	
}
