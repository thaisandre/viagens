package br.com.caelum.viagens.aeronaves.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;
import br.com.caelum.viagens.aeronaves.model.Modelo;

public class AssentosUtilsTests {
	
	@Test
	public void deveGerarAcentosDoModeloEspecificoDeAeronave() {
		
		Aeronave aeronave = new Aeronave(Modelo.ATR40);
		Set<Assento> assentos = AssentosUtils.geraAssentos(aeronave);
		
		System.out.println(assentos);
		assertThat(assentos).isNotEmpty();
		assertThat(assentos.size()).isEqualTo(Modelo.ATR40.getFileiras()*Modelo.ATR40.getAssentosPorFileira());
	}
}
