package br.com.caelum.viagens.voos.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.voos.model.Passagem;
import io.github.asouza.DataView;

public class PassagemOutputDto {
	
	public static Map<String, Object> criado(Passagem passagem) {
		return DataView.of(passagem)
				.add(Passagem::getId)
				.add("assento", Passagem::getAssento)
				.addDate("dataEHoraDePartida", p -> p.getDataEHoraDePartida(), "dd/MM/yyyy HH:mm")
				.add("valor", Passagem::getValor)
				.add("rotas",  p -> p.getVoo().getRotasEmSequenciaLogica())
				.build();
	}
	
	public static Map<String, Object> detalhes(Passagem passagem) {
		return DataView.of(passagem)
				.add(Passagem::getId)
				.add("assento", Passagem::getAssento)
				.addDate("dataEHoraDePartida", p -> p.getDataEHoraDePartida(), "dd/MM/yyyy HH:mm")
				.add("valor", Passagem::getValor)
				.add("rotas", p -> p.getVoo().getRotasEmSequenciaLogica())
				.build();
	}
}
