package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaRepetidaValidatorTests {
	
	private RotaRepetidaValidator validator;
	
	@BeforeEach
	public void setUp() {
		this.validator = new RotaRepetidaValidator();
	}
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComRotasRepetidas() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(1L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("Não é permitido repetir rotas em um voo.");
		
	}
	
	@Test
	public void naoDeveDetectarErrorNoCadastroDeVooComRotasNaoRepetidas() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(2L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isEmpty();
	}
}
