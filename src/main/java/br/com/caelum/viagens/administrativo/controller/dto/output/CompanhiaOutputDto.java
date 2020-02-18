package br.com.caelum.viagens.administrativo.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.administrativo.model.Companhia;
import io.github.asouza.DataView;

public class CompanhiaOutputDto {
	
	public static Map<String, Object> criado(Companhia companhia){ 
		return DataView.of(companhia)
			.add(Companhia::getId)
			.add(Companhia::getNome)
			.add(Companhia::getPais)
			.build();
	}
	
	public static Map<String, Object> detalhes(Companhia companhia){
		return DataView.of(companhia)
				.add(Companhia::getId)
				.add(Companhia::getNome)
				.add(Companhia::getPais)
				.build();
	}	
}
