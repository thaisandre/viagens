package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class PaisNaoExistenteValidatorTests {
	
	private PaisRepository paisRepository;
	private PaisNaoExistenteValidator validator;
	
	@BeforeEach
	public void setUp() {
		this.paisRepository = Mockito.mock(PaisRepository.class);
		when(this.paisRepository.findById(1L)).thenReturn(Optional.of(new Pais("Brasil")));
		this.validator = new PaisNaoExistenteValidator(paisRepository);
	}
	
	@Test
	public void deveDetectarErroSeIdDoPaisDoCadastroDeCompanhiaNaoExiste() {
		
		NewCompanhiaInputDto newCompanhiaDto = new NewCompanhiaInputDto();
		newCompanhiaDto.setNome("CompanhiaA");
		newCompanhiaDto.setPaisId(2L);
		
		BindingResult result = new BeanPropertyBindingResult(newCompanhiaDto, "newCompanhiaDto");
		validator.validate(newCompanhiaDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("paisId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage()).isEqualTo("País não existe no sistema.");
	}
	
	@Test
	public void deveDetectarErroSeIdDoPaisDoCadastroDeAeroportoNaoExiste() {
		
		NewAeroportoInputDto newAeroportoDto = new NewAeroportoInputDto();
		newAeroportoDto.setNome("AeroportoA");
		newAeroportoDto.setPaisId(2L);
		
		BindingResult result = new BeanPropertyBindingResult(newAeroportoDto, "newAeroportoDto");
		validator.validate(newAeroportoDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("paisId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage()).isEqualTo("País não existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroSeIdDoPaisDoCadastroDeCompanhiaExiste() {
		NewCompanhiaInputDto newCompanhiaDto = new NewCompanhiaInputDto();
		newCompanhiaDto.setNome("CompanhiaA");
		newCompanhiaDto.setPaisId(1L);
		
		BindingResult result = new BeanPropertyBindingResult(newCompanhiaDto, "newCompanhiaDto");
		validator.validate(newCompanhiaDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
	@Test
	public void naoDeveDetectarErroSeIdDoPaisDoCadastroDeAeroportoExiste() {
		NewAeroportoInputDto newAeroportoDto = new NewAeroportoInputDto();
		newAeroportoDto.setNome("AeroportoA");
		newAeroportoDto.setPaisId(1L);
		
		BindingResult result = new BeanPropertyBindingResult(newAeroportoDto, "newAeroportoDto");
		validator.validate(newAeroportoDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
}
