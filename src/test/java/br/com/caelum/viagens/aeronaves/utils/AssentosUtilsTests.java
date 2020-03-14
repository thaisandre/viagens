package br.com.caelum.viagens.aeronaves.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;
import br.com.caelum.viagens.aeronaves.model.Modelo;

public class AssentosUtilsTests {
	
	@Test
	public void deveGerarAcentosDoModeloEspecificoDeAeronave() {
		
		Aeronave aeronave = new Aeronave(Modelo.ATR40);
		Set<Assento> assentos = AssentosUtils.geraAssentos(aeronave);
		
		List<Integer> numeros= IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList());
		List<Character> letras = List.of('A', 'B', 'C', 'D');
		
		assertThat(assentos).isNotEmpty();
		assentos.forEach(assento -> assertTrue(numeros.contains(assento.getFileira())));
		assentos.forEach(assento -> assertTrue(letras.contains(assento.getPosicao())));
		assertThat(assentos.size()).isEqualTo(Modelo.ATR40.getFileiras()*Modelo.ATR40.getAssentosPorFileira());
		
	}
}
