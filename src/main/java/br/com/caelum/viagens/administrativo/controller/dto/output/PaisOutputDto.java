package br.com.caelum.viagens.administrativo.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.administrativo.model.Pais;
import io.github.asouza.DataView;

public class PaisOutputDto {
	
	public static Map<String, Object> criado(Pais pais){ 
		return DataView.of(pais)
			.add(Pais::getId)
			.add(Pais::getNome)
			.build();
	}
	
	public static Map<String, Object> detalhes(Pais pais){
		return DataView.of(pais)
				.add(Pais::getId)
				.add(Pais::getNome)
				.build();
	}
}
