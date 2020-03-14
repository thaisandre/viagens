package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;
import br.com.caelum.viagens.aeronaves.model.Modelo;
import br.com.caelum.viagens.aeronaves.repository.AssentoRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;

public class AssentoExistenteValidatorTests {
	
	private AssentoExistenteValidator validator;
	private AssentoRepository assentoRepository;
	
	@BeforeEach
	public void setUp() {
		this.assentoRepository = Mockito.mock(AssentoRepository.class);
		
		Aeronave aeronave = new Aeronave(Modelo.ATR40);
		Assento assento = new Assento(1, 'A', aeronave);

		when(this.assentoRepository.findById(1L)).thenReturn(Optional.of(assento));
		when(this.assentoRepository.findById(20L)).thenReturn(Optional.empty());
		
		this.validator = new AssentoExistenteValidator(assentoRepository);
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarPassagemComAssentoExistente() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setAssentoId(1L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarPassagemComAssentoQueNaoExisteNoSistema() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setAssentoId(20L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("assentoId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("assento não existe no sistema.");
	}
	
}
