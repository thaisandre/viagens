package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewPaisInputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class PaisExistenteValidatorTests {

	private PaisRepository paisRepository;
	private PaisExistenteValidator validator;
	
	@BeforeEach
	public void setUp() {
		this.paisRepository = Mockito.mock(PaisRepository.class);
		when(this.paisRepository.findByNome("Brasil")).thenReturn(Optional.of(new Pais("Brasil")));
		this.validator = new PaisExistenteValidator(paisRepository);
	}

	@Test
	public void deveDetectarErroQueNomeDoPaisJaExiste() {
		
		NewPaisInputDto newPaisDto = new NewPaisInputDto();
		newPaisDto.setNome("Brasil");
		
		BindingResult result = new BeanPropertyBindingResult(newPaisDto, "newPaisDto");
		validator.validate(newPaisDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("nome");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage()).isEqualTo("Pais já existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroQueNomeDoPaisJaExiste() {
		
		NewPaisInputDto newPaisDto = new NewPaisInputDto();
		newPaisDto.setNome("Argentina");
		
		BindingResult result = new BeanPropertyBindingResult(newPaisDto, "newPaisDto");
		validator.validate(newPaisDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}

}
