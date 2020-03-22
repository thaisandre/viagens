package br.com.caelum.viagens.voos.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.voos.model.Passagem;
import io.github.asouza.DataView;

public class PassagemOutputDto {
	
	public static Map<String, Object> detalhes(Passagem passagem) {
		return DataView.of(passagem)
				.add(Passagem::getId)
				.addDate("dataEHoraDePartida", p -> p.getDataEHoraDePartida(), "dd/MM/yyyy kk:mm")
				.add("valor", Passagem::getValor)
				.build();
	}	
}
