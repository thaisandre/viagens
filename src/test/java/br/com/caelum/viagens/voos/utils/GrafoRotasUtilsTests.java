package br.com.caelum.viagens.voos.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.support.Route;

public class GrafoRotasUtilsTests {
	
	private Rota rotaAtoB;
	private Rota rotaBtoA;
	private Rota rotaBtoC;
	private Rota rotaCtoU;
	private Rota rotaBtoU;
	private Rota rotaUtoA;
	
	@BeforeEach
	public void setUp() {
		Aeroporto aeroportoA = new Aeroporto("AeroportoA", new Pais("Argentina"));
		Aeroporto aeroportoB = new Aeroporto("AeroportoB", new Pais("Brasil"));
		Aeroporto aeroportoC = new Aeroporto("AeroportoC", new Pais("Chile"));
		Aeroporto aeroportoU = new Aeroporto("AeroportoU", new Pais("Uruguai"));
		
		this.rotaAtoB = new Rota(aeroportoA, aeroportoB, 120);
		this.rotaBtoA = new Rota(aeroportoB, aeroportoA, 120);
		this.rotaBtoC = new Rota(aeroportoB, aeroportoC, 120);
		this.rotaCtoU = new Rota(aeroportoC, aeroportoU, 120);
		this.rotaBtoU = new Rota(aeroportoB, aeroportoU, 120);
		this.rotaUtoA = new Rota(aeroportoU, aeroportoA, 120);
		
	}
	
	@Test
	public void deveRetornarTrueParaRotasComApenasUmaRota() {
		
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		
		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isTrue();
		
	}
	
	@Test
	public void deveRetornarTrueParaRotasDeVooEmSequenciaLogicaOrdenada() {
		
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaBtoC);
		rotas.add(this.rotaCtoU);
		
		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isTrue();
		
	}
	
	@Test
	public void deveRetornarTrueParaRotasDeVooEmSequenciaLogicaDesordenada() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaCtoU);
		rotas.add(this.rotaBtoC);
		
		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isTrue();
		
	}
	
	@Test
	public void deveRetornarTrueParaSetDeRotasComRotasRepetidasComSequenciaLogica() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaBtoU);
		rotas.add(this.rotaAtoB);
		
		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isTrue();
	}
	
	@Test
	public void deveRetornarFalseParaSetDeRotasComRotasRepetidasSemSequenciaLogica() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaCtoU);
		rotas.add(this.rotaAtoB);
		
		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isFalse();
	}
	
	@Test
	public void deveRetornarFalseParaRotasComOrigemInicialIgualADestinoFinal() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaBtoU);
		rotas.add(this.rotaUtoA);
		
		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isFalse();
	}
	
	@Test
	public void deveRetornarFalseParaRotasComDuasOrigensIguais() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaBtoC);
		rotas.add(this.rotaBtoU);
		
		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isFalse();
	}
	
	@Test
	public void deveRetornarFalseParaRotasComDoisDestinosIguais() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaBtoU);
		rotas.add(this.rotaCtoU);

		assertThat(GrafoRotasUtils.temSequenciaLogica(rotas)).isFalse();
	}
	
	@Test
	public void deveRetornarDestinoFinalCorretamente() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaBtoC);
		rotas.add(this.rotaCtoU);

		assertThat(GrafoRotasUtils.getDestinoFinal(rotas)).isEqualTo(rotaCtoU.getDestino());
	}
	
	@Test
	public void deveRetornarOrigemInicialCorretamente() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaBtoC);
		rotas.add(this.rotaCtoU);

		assertThat(GrafoRotasUtils.getOrigemInicial(rotas)).isEqualTo(rotaAtoB.getOrigem());
	}
	
	@Test
	public void deveFalharEmRetornarOrigemInicialComRotasSemSequenciaLogica() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaBtoC);
		rotas.add(this.rotaBtoA);
		rotas.add(this.rotaAtoB);
		
		Exception exception = assertThrows(RuntimeException.class, () -> {
			GrafoRotasUtils.getOrigemInicial(rotas);
		});
		
	    assertThat(exception.getMessage()).isEqualTo("Não foi possível detectar a origem inicial já que as rotas não possuem sequência lógica.");

	}
	
	@Test
	public void deveFalharEmRetornarDestinoFinalComRotasSemSequenciaLogica() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaCtoU);
		rotas.add(this.rotaBtoU);
		
		Exception exception = assertThrows(RuntimeException.class, () -> {
			GrafoRotasUtils.getDestinoFinal(rotas);
		});
		
	    assertThat(exception.getMessage()).isEqualTo("Não foi possível detectar o destino final já que as rotas não possuem sequência lógica.");

	}
	
	@Test
	public void deveRetonarRotasEmSequenciaLogica() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaAtoB);
		rotas.add(this.rotaCtoU);
		rotas.add(this.rotaBtoC);
		
		List<Rota> rotasEmSequenciaLogica = 
				GrafoRotasUtils.getRotasEmSequenciaLogica(rotas);
		
		assertThat(rotasEmSequenciaLogica).isNotEmpty();
		assertThat(rotasEmSequenciaLogica.get(0)).isEqualTo(rotaAtoB);
		assertThat(rotasEmSequenciaLogica.get(1)).isEqualTo(rotaBtoC);
		assertThat(rotasEmSequenciaLogica.get(2)).isEqualTo(rotaCtoU);
	}
	
	@Test
	public void deveFalharAoRetornarRotasEmSequenciaLogica() {
		Set<Route> rotas = new HashSet<>();
		rotas.add(this.rotaBtoU);
		rotas.add(this.rotaBtoC);
		rotas.add(this.rotaCtoU);
		
		Exception exception = assertThrows(RuntimeException.class, () -> {
				GrafoRotasUtils.getRotasEmSequenciaLogica(rotas);
		});
		
		
		assertThat(exception.getMessage()).isEqualTo("A lista de rotas não possui uma sequência lógica.");
	}
	
}
