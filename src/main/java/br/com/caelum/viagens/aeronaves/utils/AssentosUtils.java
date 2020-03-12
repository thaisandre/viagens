package br.com.caelum.viagens.aeronaves.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;

public class AssentosUtils {
	
	private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static Set<Assento> geraAssentos(Aeronave aeronave) {
		Set<Assento> assentos = new HashSet<>();
		IntStream.rangeClosed(1,  aeronave.getModelo().getFileiras()).forEach(fileira -> {
			IntStream.range(0, aeronave.getModelo().getAssentosPorFileira()).forEach(posicao -> {
				assentos.add(new Assento(fileira, getCharacter(posicao), aeronave));
			});
		});
		return assentos;
	}
	
	private static Character getCharacter(Integer posicao) {
		return Character.valueOf(ALFABETO.charAt(posicao));
	}
}
