package br.com.caelum.viagens.administrativo.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.administrativo.model.Rota;
import io.github.asouza.DataView;

public class RotaOutputDto {
	
	public static Map<String, Object> rotaCriada(Rota rota){ 
		return DataView.of(rota)
			.add(Rota::getId)
			.add(Rota::getNome)
			.add(Rota::getOrigem)
			.add(Rota::getDestino)
			.add(Rota::getDuracao)
			.build();
	}
	
	public static Map<String, Object> detalhesRota(Rota rota){
		return DataView.of(rota)
				.add(Rota::getId)
				.add(Rota::getNome)
				.add(Rota::getOrigem)
				.add(Rota::getDestino)
				.add(Rota::getDuracao)
				.build();
	}
	
	
	

}
