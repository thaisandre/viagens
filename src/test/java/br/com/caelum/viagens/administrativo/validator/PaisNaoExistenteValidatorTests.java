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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PaisNaoExistenteValidatorTests {
	
	@Autowired
	private PaisRepository paisRepository;
	
	private PaisNaoExistenteValidator validator;
	
	private Pais brasil;
	
	@BeforeEach
	public void setUp() {
		this.brasil = this.paisRepository.save(new Pais("Brasil"));
		this.validator = new PaisNaoExistenteValidator(paisRepository);
	}

	@Test
	public void deveDetectarErroSeIdDoPaisNaoExisteNoCadastroDeCompanhia() {
		
		NewCompanhiaInputDto newCompanhiaDto = new NewCompanhiaInputDto();
		newCompanhiaDto.setNome("CompanhiaA");
		newCompanhiaDto.setPaisId(20L);
		
		BindingResult errors = new BeanPropertyBindingResult(newCompanhiaDto, "newCompanhiaDto");
		validator.validate(newCompanhiaDto, errors);
		
		assertThat(errors.getFieldErrors()).isNotEmpty();
		assertThat(errors.getFieldErrors().get(0).getField()).isEqualTo("paisId");
		assertThat(errors.getFieldErrors().get(0).getDefaultMessage()).isEqualTo("País não existe no sistema.");
	}
	
	@Test
	public void deveDetectarErroSeIdDoPaisNaoExisteNoCadastroDeAeroporto() {
		
		NewAeroportoInputDto newAeroportoDto = new NewAeroportoInputDto();
		newAeroportoDto.setNome("AeroportoA");
		newAeroportoDto.setPaisId(20L);
		
		BindingResult errors = new BeanPropertyBindingResult(newAeroportoDto, "newAeroportoDto");
		validator.validate(newAeroportoDto, errors);
		
		assertThat(errors.getFieldErrors()).isNotEmpty();
		assertThat(errors.getFieldErrors().get(0).getField()).isEqualTo("paisId");
		assertThat(errors.getFieldErrors().get(0).getDefaultMessage()).isEqualTo("País não existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroSeIdDoPaisExisteNoCadastroDeCompanhia() {
		
		NewCompanhiaInputDto newCompanhiaDto = new NewCompanhiaInputDto();
		newCompanhiaDto.setNome("CompanhiaA");
		newCompanhiaDto.setPaisId(this.brasil.getId());
		
		BindingResult errors = new BeanPropertyBindingResult(newCompanhiaDto, "newCompanhiaDto");
		validator.validate(newCompanhiaDto, errors);
		
		assertThat(errors.getFieldErrors()).isEmpty();
	}
	
	@Test
	public void naoDeveDetectarErroSeIdDoPaisExisteNoCadastroDeAeroporto() {
		
		NewAeroportoInputDto newAeroportoDto = new NewAeroportoInputDto();
		newAeroportoDto.setNome("AeroportoA");
		newAeroportoDto.setPaisId(this.brasil.getId());
		
		BindingResult errors = new BeanPropertyBindingResult(newAeroportoDto, "newAeroportoDto");
		validator.validate(newAeroportoDto, errors);
		
		assertThat(errors.getFieldErrors()).isEmpty();
	}
}
