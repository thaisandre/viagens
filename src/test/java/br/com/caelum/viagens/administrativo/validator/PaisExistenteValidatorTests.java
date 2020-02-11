package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewPaisInputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PaisExistenteValidatorTests {

	@Autowired
	private PaisRepository paisRepository;
	
	private PaisExistenteValidator validator;
	
	@Valid
	private NewPaisInputDto paisValido;
	
	@BeforeEach
	public void setUp() {
		this.paisRepository.save(new Pais("Brasil"));
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
