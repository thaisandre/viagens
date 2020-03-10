package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaRepetidaValidatorTests {
	
	private RotaRepetidaValidator validator;
	private RotaRepository rotaRepository;
	
	@BeforeEach
	public void setUp() {
		this.rotaRepository = Mockito.mock(RotaRepository.class);
		
		Aeroporto aeroportoA = new Aeroporto("AeroportoA", new Pais("Argentina"));
		Aeroporto aeroportoB = new Aeroporto("AeroportoB", new Pais("Brasil"));
		Aeroporto aeroportoC = new Aeroporto("AeroportoC", new Pais("Chile"));
		
		Rota rota1AtoB = new Rota(aeroportoA, aeroportoB, 120);
		Rota rota2BtoC = new Rota(aeroportoB, aeroportoC, 120);
	
		when(this.rotaRepository.findAllById(Arrays.asList(1L, 1L))).thenReturn(Arrays.asList(rota1AtoB));
		when(this.rotaRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(rota1AtoB, rota2BtoC));
		
		this.validator = new RotaRepetidaValidator(rotaRepository);
	}
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComRotasRepetidas() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(1L);
		
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
			.isEqualTo("não é permitido repetir rotas em um voo.");
		
	}
	
	@Test
	public void naoDeveDetectarErrorNoCadastroDeVooComRotasNaoRepetidas() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(2L);
		
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
