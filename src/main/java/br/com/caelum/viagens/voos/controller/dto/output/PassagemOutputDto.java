package br.com.caelum.viagens.voos.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.voos.model.Passagem;
import io.github.asouza.DataView;

public class PassagemOutputDto {
	
	public static Map<String, Object> detalhes(Passagem passagem) {
		return DataView.of(passagem)
				.add(Passagem::getId)
				.add("assento", Passagem::getAssento)
				.addDate("dataEHoraDePartida", p -> p.getDataEHoraDePartida(), "dd/MM/yyyy kk:mm")
				.add("valor", Passagem::getValor)
				.build();
	}

	public static Map<String, Object> atualizada(Passagem passagem) {
		return DataView.of(passagem)
				.add(Passagem::getId)
				.add("assento", Passagem::getAssento)
				.addDate("dataEHoraDePartida", p -> p.getDataEHoraDePartida(), "dd/MM/yyyy kk:mm")
				.add("valor", Passagem::getValor)
				.build();
	}
	
}
