package br.com.caelum.viagens.voos.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.util.Assert;

import br.com.caelum.viagens.administrativo.model.Companhia;

@Entity
public class Voo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Cascade(CascadeType.PERSIST)
	@ManyToMany
	private Set<Rota> rotas;
	
	@NotNull
	@ManyToOne
	private Companhia companhia;
	
	@NotNull
	@Positive
	private Integer lugaresDisponiveis;
	
	@Deprecated
	public Voo() {}

	public Voo(@NotNull Set<Rota> rotas, @NotNull Companhia companhia, @NotNull @Positive Integer lugaresDisponiveis) {
		Assert.notNull(rotas, "A lista de rotas não pode ser nula");
		Assert.notEmpty(rotas, "A lista de rotas não pode ser vazia");
		Assert.notNull(companhia, "A companhia não pode ser nula");
		Assert.notNull(lugaresDisponiveis, "O número de lugares disponíveis não pode ser nulo");
		Assert.isTrue(lugaresDisponiveis > 0, () -> "O número de lugares disponíveis deve ser positivo.");
		
		this.rotas = rotas;
		this.companhia = companhia;
		this.lugaresDisponiveis = lugaresDisponiveis;
	}

}
