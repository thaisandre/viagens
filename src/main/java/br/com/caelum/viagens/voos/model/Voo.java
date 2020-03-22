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

import org.springframework.util.Assert;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;
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
	@ManyToOne
	private Aeronave aeronave;
	
	@OneToMany(mappedBy = "voo", cascade = CascadeType.ALL)
	private Set<Passagem> passagens = new HashSet<>();

	@Deprecated
	public Voo() {
	}

	public Voo(@NotNull Set<RotaSemVoo> rotas, @NotNull Companhia companhia, @NotNull Aeronave aeronave) {
		Assert.notNull(rotas, "a lista de rotas não pode ser nula.");
		Assert.notEmpty(rotas, "a lista de rotas não pode ser vazia.");
		Assert.isTrue(GrafoRotasUtils.temSequenciaLogica(rotas),
				"rotas devem possuir uma sequência lógica.");
		Assert.notNull(companhia, "a companhia não pode ser nula.");
		Assert.notNull(aeronave, "a aeronave não pode ser nula.");

		this.rotas = rotas.stream().map(r -> new Rota(r.getRota(), this, r.getParada())).collect(Collectors.toSet());
		Assert.isTrue(temApenasUmaPernaFinal(), "rotas deve conter uma única perna final.");
		
		this.companhia = companhia;
		this.aeronave = aeronave;
	}

	public Long getId() {
		return id;
	}
	
	public Aeronave getAeronave() {
		return aeronave;
	}

	public String getNomeCompanhia() {
		return companhia.getNome();
	}
	
	public Integer getLugaresDisponiveis() {
		return aeronave.getAssentos().size() - passagens.size();
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
	
	public boolean contem(Assento assento) {
		return this.aeronave.getAssentos().contains(assento);
	}
	
	public Set<Passagem> getPassagens() {
		return passagens;
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
