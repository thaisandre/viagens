package br.com.caelum.viagens.administrativo.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import io.github.asouza.DataView;

public class AeroportoOutputDto {

	public static Map<String, Object> criado(Aeroporto aeroporto) {
		return DataView.of(aeroporto)
				.add(Aeroporto::getId)
				.add(Aeroporto::getNome)
				.add(Aeroporto::getPais)
				.build();
	}

	public static Map<String, Object> detalhes(Aeroporto aeroporto) {
		return DataView.of(aeroporto)
				.add(Aeroporto::getId)
				.add(Aeroporto::getNome)
				.add(Aeroporto::getPais)
				.build();
	}
}
