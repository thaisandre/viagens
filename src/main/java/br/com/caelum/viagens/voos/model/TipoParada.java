package br.com.caelum.viagens.voos.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoParada {
	
	ESCALA,
	CONEXAO;
	
	public static List<String> tiposParada() {
		return Arrays.asList(TipoParada.values()).stream().map(r -> r.toString()).collect(Collectors.toList());
	}

}
