package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class NomeCompanhiaExistenteValidatorTests {
	
	@Autowired
	private CompanhiaRepository companhiaRepository;
	
	@Autowired
	private PaisRepository paisRepository;
	
	private NomeCompanhiaExistenteValidator validator;
	
	private Pais argentina;
	
	@BeforeEach
	public void setUp() {
		this.argentina = this.paisRepository.save(new Pais("Argentina"));
		this.companhiaRepository.save(new Companhia("CompanhiaA", this.argentina));
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
