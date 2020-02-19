package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;

public class AeroportosOrigemEDestinoDiferentesValidatorTests {

	private AeroportosOrigemEDestinoDiferentesValidator validator;

	@BeforeEach
	public void setUp() {
		this.validator = new AeroportosOrigemEDestinoDiferentesValidator();
	}

	@Test
	public void deveDetectarErroSeOrigemIgualADestino() {
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(1L);
		newRotaDto.setDestinoId(1L);
		newRotaDto.setDuracao(120);

		BindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);

		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
				.isEqualTo("Aeroportos de origem e destino não podem ser iguais.");
	}

	@Test
	public void naoDeveDetectarErroSeOrigemDiferenteDeADestino() {
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(1L);
		newRotaDto.setDestinoId(2L);
		newRotaDto.setDuracao(120);

		BindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);

		assertThat(result.getGlobalErrors()).isEmpty();
	}

}
