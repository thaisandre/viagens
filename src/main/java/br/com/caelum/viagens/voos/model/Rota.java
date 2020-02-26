package br.com.caelum.viagens.voos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.util.Assert;

@Entity(name="rota_voo")
public class Rota {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne
	private br.com.caelum.viagens.administrativo.model.Rota rota;
	
	@Cascade(CascadeType.PERSIST)
	@OneToOne
	private Parada parada;
	
	public Rota(@NotNull br.com.caelum.viagens.administrativo.model.Rota rota, Parada parada) {
		Assert.notNull(rota, "A rota não pode ser nula.");
		this.rota = rota;
		this.parada = parada;
	}
	
	@Deprecated
	public Rota() {}
}
