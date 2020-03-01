package br.com.caelum.viagens.voos.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.util.Assert;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.support.Route;
import br.com.caelum.viagens.voos.utils.GrafoRotasUtils;

@Entity
public class Voo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@OneToMany(mappedBy = "voo", cascade = CascadeType.ALL)
	private Set<Rota> rotas = new HashSet<>();
	
	@NotNull
	@ManyToOne
	private Companhia companhia;
	
	@NotNull
	@Positive
	private Integer lugaresDisponiveis;
	
	@Deprecated
	public Voo() {}

	public Voo(@NotNull Set<Rota> rotas, @NotNull Companhia companhia, @NotNull @Positive Integer lugaresDisponiveis) {
		System.out.println("no contrutor " + rotas);
		Assert.notNull(rotas, "A lista de rotas não pode ser nula");
		Assert.notEmpty(rotas, "A lista de rotas não pode ser vazia");
		Assert.notNull(companhia, "A companhia não pode ser nula");
		Assert.notNull(lugaresDisponiveis, "O número de lugares disponíveis não pode ser nulo");
		Assert.isTrue(lugaresDisponiveis > 0, () -> "O número de lugares disponíveis deve ser positivo.");
		
		this.rotas = rotas;
		this.rotas.forEach(r -> r.setVoo(this));
		this.companhia = companhia;
		this.lugaresDisponiveis = lugaresDisponiveis;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getNomeCompanhia() {
		return companhia.getNome();
	}
	
	public Integer getLugaresDisponiveis() {
		return lugaresDisponiveis;
	}
	
	public Set<Rota> getRotas() {
		return rotas;
	}
	
	public String getNomeDaOrigem() {
		return getOrigem().getNome();
	}
	
	public String getNomeDoDestino() {
		return getDestino().getNome();
	}
	
	public List<Rota> getRotasEmSequenciaLogica() {
		List<Rota> rotasEmSequenciaLogica = new ArrayList<>();
		GrafoRotasUtils.getCaminhos(toListofRoute(rotas), 
				getOrigem(), getDestino()).forEach(path -> {
			path.getEdgeList().forEach(e -> rotasEmSequenciaLogica.add((Rota) e));
		});
		return rotasEmSequenciaLogica;
	}
	
	private Aeroporto getDestino() {
		return GrafoRotasUtils.getDestino(toListofRoute(this.rotas));
	}
	
	private Aeroporto getOrigem() {
		return GrafoRotasUtils.getOrigem(toListofRoute(this.rotas));
	}
	
	private List<Route> toListofRoute(Set<Rota> rotas){
		return rotas.stream().map(r -> r).collect(Collectors.toList());
	}
}
