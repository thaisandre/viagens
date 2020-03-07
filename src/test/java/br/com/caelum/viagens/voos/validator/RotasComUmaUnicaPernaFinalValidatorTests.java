package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.voos.controller.dto.input.NewParadaDaRotaInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotasComUmaUnicaPernaFinalValidatorTests {
	
	private RotasComUmaUnicaPernaFinalValidator validator;
	
	@BeforeEach
	public void setUp() {
		this.validator = new RotasComUmaUnicaPernaFinalValidator();
	}
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComMaisDeUmaRotaSemParada() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rota1);
		rotas.add(rota2);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("rotas");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("a lista de rotas precisa conter apenas uma única rota final (sem parada).");
	}

	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComRotasSemPernaFinal() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setParada(parada);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setParada(parada);
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rota1);
		rotas.add(rota2);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("rotas");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("a lista de rotas precisa conter pelo menos uma rota final (sem parada).");
	}
	
	@Test
	public void naoDeveDetectarErrorNoCadastroDeVooComRotasComApenasUmaRotaSendoPernaFinal() {
		NewRotaDoVooInputDto rota = new NewRotaDoVooInputDto();
				
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rota);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
	public void naoDeveDetectarErrorNoCadastroDeVooComRotasComApenasUmaPernaFinal() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setParada(parada);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rota1);
		rotas.add(rota2);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}


}
