package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaExistenteValidatorTests {
	
	private RotaExistenteValidator validator;
	private RotaRepository rotaRepository;
	
	@BeforeEach
	public void setUp() {
		rotaRepository = Mockito.mock(RotaRepository.class);
		Rota rota = Mockito.mock(Rota.class);
		
		when(rota.getId()).thenReturn(1L);
		when(rotaRepository.findAllById(Set.of(1L))).thenReturn(Set.of(rota));
		when(rotaRepository.findAllById(Set.of(10L))).thenReturn(Set.of());
		
		this.validator = new RotaExistenteValidator(rotaRepository);
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarVooComUmaRotaQueNaoExiste() {
		NewRotaDoVooInputDto rota = new NewRotaDoVooInputDto();
		rota.setRotaId(10L);
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rota);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("rotas");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("rotaId 10 não existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarVooComRotaExistente() {
		NewRotaDoVooInputDto rota = new NewRotaDoVooInputDto();
		rota.setRotaId(1L);

		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rota);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}

}
