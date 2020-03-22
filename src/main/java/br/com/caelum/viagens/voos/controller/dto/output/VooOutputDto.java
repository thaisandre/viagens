package br.com.caelum.viagens.voos.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.voos.model.Voo;
import io.github.asouza.DataView;

public class VooOutputDto {
	
	public static Map<String, Object> criado(Voo voo) {
		return DataView.of(voo)
				.add(Voo::getId)
				.add("rotas", Voo::getRotasEmSequenciaLogica)
				.add("origemInicial", Voo::getNomeDaOrigem)
				.add("destinoFinal", Voo::getNomeDoDestino)
				.add(Voo::getNomeCompanhia)
				.add(Voo::getLugaresDisponiveis)
				.build();
	}
	
	public static Map<String, Object> detalhes(Voo voo) {
		return DataView.of(voo)
				.add(Voo::getId)
				.add("origemInicial", Voo::getNomeDaOrigem)
				.add("destinoFinal", Voo::getNomeDoDestino)
				.add("rotas", Voo::getRotasEmSequenciaLogica)
				.add(Voo::getNomeCompanhia)
				.add(Voo::getLugaresDisponiveis)
				.build();
	}
	
	public static Map<String, Object> passagensGeradas(Voo voo) {
		return DataView.of(voo)
				.addCollection("passagens", Voo::getPassagens, PassagemOutputDto::detalhes)
				.build();
	}
}
