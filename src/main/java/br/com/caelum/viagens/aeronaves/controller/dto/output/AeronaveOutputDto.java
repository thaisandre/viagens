package br.com.caelum.viagens.aeronaves.controller.dto.output;

import java.util.Map;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import io.github.asouza.DataView;

public class AeronaveOutputDto {
	
	public static Map<String, Object> criado(Aeronave aeronave) {
		return DataView.of(aeronave)
				.add("id", Aeronave::getId)
				.add("modelo", Aeronave::getModelo)
				.add("assentos", Aeronave::getAssentos)
				.build();
	}
	
	public static Map<String, Object> detalhes(Aeronave aeronave) {
		return DataView.of(aeronave)
				.add("id", Aeronave::getId)
				.add("modelo", Aeronave::getModelo)
				.add("assentos", Aeronave::getAssentos)
				.build();
	}
}
