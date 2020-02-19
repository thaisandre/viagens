package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;

public class RotaExistenteValidatorTests {
	
	private RotaExistenteValidator validator;
	private RotaRepository rotaRepository;
	
	@BeforeEach
	public void setUp() {
		this.rotaRepository = Mockito.mock(RotaRepository.class);		
		Aeroporto aeroportoA = new Aeroporto("AeroportoA", new Pais("Argentina"));
		Aeroporto aeroportoB = new Aeroporto("AeroportoB", new Pais("Brasil"));
		
		Rota rota = new Rota(aeroportoA, aeroportoB, 90);
		when(this.rotaRepository.findByOrigemIdAndDestinoId(1L, 2L)).thenReturn(Optional.of(rota));
		
		this.validator = new RotaExistenteValidator(rotaRepository);
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarRotaQueJaExiste() {
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(1L);
		newRotaDto.setDestinoId(2L);
		newRotaDto.setDuracao(90);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("Rota já existe no sistema.");

	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarRotaQueNaoExiste() {
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(1L);
		newRotaDto.setDestinoId(1L);
		newRotaDto.setDuracao(90);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);
		
		assertThat(result.getGlobalErrors()).isEmpty();

	}
}
