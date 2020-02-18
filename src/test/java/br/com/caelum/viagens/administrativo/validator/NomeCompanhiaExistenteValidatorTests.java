package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;

public class NomeCompanhiaExistenteValidatorTests {
	
	private CompanhiaRepository companhiaRepository;
	private NomeCompanhiaExistenteValidator validator;
	
	@BeforeEach
	public void setUp() {
		this.companhiaRepository = Mockito.mock(CompanhiaRepository.class);
		Companhia companhia = new Companhia("CompanhiaA", new Pais("Argentina"));
		when(this.companhiaRepository.findByNome("CompanhiaA")).thenReturn(Optional.of(companhia));
		
		this.validator = new NomeCompanhiaExistenteValidator(this.companhiaRepository);
	}
	
	@Test
	public void deveDetectarErroQueNomeDaCompanhiaJaExiste() {
		
		NewCompanhiaInputDto newCompanhiaDto = new NewCompanhiaInputDto();
		newCompanhiaDto.setNome("CompanhiaA");
		
		BindingResult result = new BeanPropertyBindingResult(newCompanhiaDto, "newCompanhiaDto");
		validator.validate(newCompanhiaDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("nome");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("Companhia de mesmo nome já existe no sistema.");
		
	}
	
	@Test
	public void naoDeveDetectarErroQueNomeDaCompanhiaJaExiste() {
		
		NewCompanhiaInputDto newCompanhiaDto = new NewCompanhiaInputDto();
		newCompanhiaDto.setNome("CompanhiaB");
		
		BindingResult result = new BeanPropertyBindingResult(newCompanhiaDto, "newCompanhiaDto");
		validator.validate(newCompanhiaDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
}
