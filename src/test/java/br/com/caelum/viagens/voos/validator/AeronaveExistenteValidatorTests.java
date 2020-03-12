package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Modelo;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class AeronaveExistenteValidatorTests {
	
	private AeronaveExistenteValidator validator;
	private AeronaveRepository aeronaveRepository;
	
	@BeforeEach
	public void setUp() {
		this.aeronaveRepository = Mockito.mock(AeronaveRepository.class);
		
		Aeronave aeronave = new Aeronave(Modelo.ATR40);
		when(this.aeronaveRepository.findById(1L)).thenReturn(Optional.of(aeronave));
		when(this.aeronaveRepository.findById(20L)).thenReturn(Optional.empty());
		
		this.validator = new AeronaveExistenteValidator(aeronaveRepository);
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarVooComAeronaveQueNaoExiste() {
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setAeronaveId(20L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("aeronaveId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("aeronave não existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarVooComAeronaveQueExiste() {
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setAeronaveId(1L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
}
