package br.com.caelum.viagens.aeronaves.model;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

public enum Modelo {
	
	A319(24, 6), A320(29, 6), A321(35, 6), A330(40, 6),
	ATR72(17, 4), ATR40(12, 4),
	B737(20, 6), B767(34, 6), B777(42, 8), B787(31, 8),
	E170(18, 4), E175(22, 4), E190(25, 4), E195(25, 4);

	private Integer fileiras;
	private Integer assentosPorFileira;
	private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	Modelo(Integer fileiras, Integer assentosPorFileira) {
		this.fileiras = fileiras;
		this.assentosPorFileira = assentosPorFileira;
	}
	
	public Integer getFileiras() {
		return fileiras;
	}

	public Integer getAssentosPorFileira() {
		return assentosPorFileira;
	}
	
	SortedSet<Assento> geraAssentos(Aeronave aeronave) {
		SortedSet<Assento> assentos = new TreeSet<>();
		IntStream.rangeClosed(1,  this.fileiras).forEach(fileira -> {
			IntStream.range(0, this.assentosPorFileira).forEach(posicao -> {
				assentos.add(new Assento(fileira, getCharacter(posicao), aeronave));
			});
		});
		return assentos;
	}
	
	private static Character getCharacter(Integer posicao) {
		return Character.valueOf(ALFABETO.charAt(posicao));
	}
	
}
