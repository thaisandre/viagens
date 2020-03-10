package br.com.caelum.viagens.voos.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.support.PossuiOrigemEDestino;

@Entity(name="rota_voo")
public class Rota implements PossuiOrigemEDestino {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne
	private br.com.caelum.viagens.administrativo.model.Rota rota;
	
	@Embedded
	private Parada parada;
	
	@NotNull
	@ManyToOne
	private Voo voo;
	
	public Rota(@NotNull br.com.caelum.viagens.administrativo.model.Rota rota, @NotNull Voo voo) {
		this(rota, voo, null);
	}
	
	public Rota(@NotNull br.com.caelum.viagens.administrativo.model.Rota rota, @NotNull Voo voo, Parada parada) {
		Assert.notNull(rota, "A rota não pode ser nula.");
		Assert.notNull(voo, "O voo não pode ser nulo.");
		this.voo = voo;
		this.rota = rota;
		this.parada = parada;
	}
	
	@Deprecated
	public Rota() {}
	
	
	public boolean isPernaFinal() {
		return this.parada == null;
	}
	
	public Aeroporto getDestino() {
		return rota.getDestino();
	}
	
	public Aeroporto getOrigem() {
		return rota.getOrigem();
	}
	
	public Parada getParada() {
		return parada;
	}

	public void setParada(Parada parada) {
		this.parada = parada;
	}

}
