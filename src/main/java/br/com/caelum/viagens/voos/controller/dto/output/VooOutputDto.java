package br.com.caelum.viagens.voos.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.voos.model.Voo;
import io.github.asouza.DataView;

public class VooOutputDto {
	
	public static Map<String, Object> criado(Voo voo) {
		return DataView.of(voo)
				.add(Voo::getId)
				.add(Voo::getRotas)
				.add(Voo::getNomeCompanhia)
				.add(Voo::getLugaresDisponiveis)
				.build();
	}
	
	public static Map<String, Object> detalhes(Voo voo) {
		return DataView.of(voo)
				.add(Voo::getId)
				.add(Voo::getRotas)
				.add(Voo::getNomeCompanhia)
				.add(Voo::getLugaresDisponiveis)
				.build();
	}
}
