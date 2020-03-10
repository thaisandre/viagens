package br.com.caelum.viagens.voos.model;

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
	public Voo() {
	}

	public Voo(@NotNull Set<RotaSemVoo> rotas, @NotNull Companhia companhia, @NotNull @Positive Integer lugaresDisponiveis) {
		Assert.notNull(rotas, "A lista de rotas não pode ser nula");
		Assert.notEmpty(rotas, "A lista de rotas não pode ser vazia");
		Assert.isTrue(GrafoRotasUtils.temSequenciaLogica(rotas),
				"rotas devem possuir uma sequência lógica.");
		Assert.notNull(companhia, "A companhia não pode ser nula");
		Assert.notNull(lugaresDisponiveis, "O número de lugares disponíveis não pode ser nulo");
		Assert.isTrue(lugaresDisponiveis > 0, () -> "O número de lugares disponíveis deve ser positivo.");

		this.rotas = rotas.stream().map(r -> new Rota(r.getRota(), this, r.getParada())).collect(Collectors.toSet());
		Assert.isTrue(temApenasUmaPernaFinal(), "rotas deve conter uma única perna final.");
		
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

	public String getNomeDaOrigem() {
		return getOrigemInicial().getNome();
	}

	public String getNomeDoDestino() {
		return getDestinoFinal().getNome();
	}

	public List<Rota> getRotasEmSequenciaLogica() {
		return GrafoRotasUtils.getRotasEmSequenciaLogica(rotas);
	}
	
	private boolean temApenasUmaPernaFinal() {
		return this.rotas.stream().filter(r -> r.isPernaFinal()).count() == 1;
	}

	private Aeroporto getDestinoFinal() {
		return GrafoRotasUtils.getDestinoFinal(this.rotas);
	}

	private Aeroporto getOrigemInicial() {
		return GrafoRotasUtils.getOrigemInicial(this.rotas);
	}
}
