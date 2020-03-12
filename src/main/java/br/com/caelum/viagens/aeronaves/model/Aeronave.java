package br.com.caelum.viagens.aeronaves.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import br.com.caelum.viagens.aeronaves.utils.AssentosUtils;

@Entity
public class Aeronave {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Modelo modelo;
	
	@NotEmpty
	@OneToMany(mappedBy = "aeronave", cascade = CascadeType.ALL)
	private Set<Assento> assentos = new TreeSet<>();
	
	@Deprecated
	public Aeronave() {}

	public Aeronave(@NotNull Modelo modelo) {
		Assert.notNull(modelo, "modelo não pode ser nulo.");
		this.modelo = modelo;
		this.assentos = AssentosUtils.geraAssentos(this);
	}
	
	public Long getId() {
		return id;
	}
	
	public Modelo getModelo() {
		return modelo;
	}
	
	public Set<Assento> getAssentos() {
		return assentos;
	}
}
