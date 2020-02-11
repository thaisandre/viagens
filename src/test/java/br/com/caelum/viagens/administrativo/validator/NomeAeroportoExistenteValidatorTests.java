package br.com.caelum.viagens.administrativo.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

public class NomeAeroportoExistenteValidatorTests {
	
	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	@Autowired
	private PaisRepository paisRepository;
	
	private NomeAeroportoExistenteValidator validator;
	
	private Pais argentina;
	
	@BeforeEach
	public void setUp() {
		this.argentina = this.paisRepository.save(new Pais("Argentina"));
		this.aeroportoRepository.save(new Aeroporto("AeroportoA", this.argentina));
		this.validator = new NomeAeroportoExistenteValidator(this.aeroportoRepository);
	}
	
	@Test
	public void deveDetectarErroQueNomeDoAeroportoJaExiste() {
		
		NewAeroportoInputDto newAeroportoDto = new NewAeroportoInputDto();
		newAeroportoDto.setNome("AeroportoA");
		
		BindingResult result = new BeanPropertyBindingResult(newAeroportoDto, "newAeroportoDto");
		validator.validate(newAeroportoDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("nome");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("Aeroporto de mesmo nome já existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroQueNomeDoAeroportoJaExiste() {
		
		NewAeroportoInputDto newAeroportoDto = new NewAeroportoInputDto();
		newAeroportoDto.setNome("AeroportoB");
		
		BindingResult result = new BeanPropertyBindingResult(newAeroportoDto, "newAeroportoDto");
		validator.validate(newAeroportoDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
}
