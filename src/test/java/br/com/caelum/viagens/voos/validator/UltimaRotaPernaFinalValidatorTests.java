package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.voos.controller.dto.input.NewParadaDaRotaInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class UltimaRotaPernaFinalValidatorTests {

	private UltimaRotaPernaFinalValidator validator;

	@BeforeEach
	public void setUp() {
		this.validator = new UltimaRotaPernaFinalValidator();
	}

	@Test
	public void deveCadastrarVooComUltimaRotaSendoAUnicaPernaFinal() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();

		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setParada(parada);

		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota1.setParada(parada);

		NewRotaDoVooInputDto rota3 = new NewRotaDoVooInputDto();
		NewVooInputDto newVooDto = new NewVooInputDto();

		newVooDto.getRotas().add(rota1);
		newVooDto.getRotas().add(rota2);
		newVooDto.getRotas().add(rota3);

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);

		assertThat(result.getGlobalErrors()).isEmpty();

	}

	@Test
	public void naoDeveCadastrarVooComPrimeiraRotaSendoAPernaFinal() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();

		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();

		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setParada(parada);

		NewRotaDoVooInputDto rota3 = new NewRotaDoVooInputDto();
		rota3.setParada(parada);

		NewVooInputDto newVooDto = new NewVooInputDto();

		newVooDto.getRotas().add(rota1);
		newVooDto.getRotas().add(rota2);
		newVooDto.getRotas().add(rota3);

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);

		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
				.isEqualTo("A última rota deve ser perna final.");
	}

	@Test
	public void naoDeveCadastrarVooComRotaEntrePrimeiraEUltimaSendoAPernaFinal() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();

		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setParada(parada);

		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();

		NewRotaDoVooInputDto rota3 = new NewRotaDoVooInputDto();
		rota3.setParada(parada);

		NewVooInputDto newVooDto = new NewVooInputDto();

		newVooDto.getRotas().add(rota1);
		newVooDto.getRotas().add(rota2);
		newVooDto.getRotas().add(rota3);

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);

		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
				.isEqualTo("A última rota deve ser perna final.");
	}

}
