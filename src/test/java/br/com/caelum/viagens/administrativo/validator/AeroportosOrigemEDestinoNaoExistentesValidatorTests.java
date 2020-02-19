package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;

public class AeroportosOrigemEDestinoNaoExistentesValidatorTests {
	
	private AeroportosOrigemEDestinoNaoExistentesValidator validator;
	private AeroportoRepository aeroportoRepository;
	
	@BeforeEach
	public void setUp() {
		this.aeroportoRepository = Mockito.mock(AeroportoRepository.class);
		Aeroporto aeroportoA = new Aeroporto("AeroportoA", new Pais("Argentina"));
		Aeroporto aeroportoB = new Aeroporto("AeroportoB", new Pais("Brasil"));
		when(this.aeroportoRepository.findById(1L)).thenReturn(Optional.of(aeroportoA));
		when(this.aeroportoRepository.findById(2L)).thenReturn(Optional.of(aeroportoB));
		
		this.validator = new AeroportosOrigemEDestinoNaoExistentesValidator(this.aeroportoRepository);
	}
	

	@Test
	public void deveDetectarErroSeOrigemNaoExistir() {
		
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(4L);
		newRotaDto.setDestinoId(1L);
		newRotaDto.setDuracao(120);
		
		BindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("origemId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("Aeroporto origem não existe no sistema.");
	}
	
	@Test
	public void deveDetectarErroSeDestinoNaoExistir() {
		
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(1L);
		newRotaDto.setDestinoId(4L);
		newRotaDto.setDuracao(120);
		
		BindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("destinoId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("Aeroporto destino não existe no sistema.");
	}
	
	@Test
	public void deveDetectarErroSeOrigemEDestinoNaoExistir() {
		
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(3L);
		newRotaDto.setDestinoId(4L);
		newRotaDto.setDuracao(120);
		
		BindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("origemId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("Aeroporto origem não existe no sistema.");
		assertThat(result.getFieldErrors().get(1).getField()).isEqualTo("destinoId");
		assertThat(result.getFieldErrors().get(1).getDefaultMessage())
			.isEqualTo("Aeroporto destino não existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroSeOrigemEDestinoExistirem() {
		
		NewRotaInputDto newRotaDto = new NewRotaInputDto();
		newRotaDto.setOrigemId(1L);
		newRotaDto.setDestinoId(2L);
		newRotaDto.setDuracao(120);
		
		BindingResult result = new BeanPropertyBindingResult(newRotaDto, "newRotaDto");
		validator.validate(newRotaDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
}
