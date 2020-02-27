package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class ComapanhiaExistenteValidatorTests {
	
	private CompanhiaExistenteValidator validator;
	private CompanhiaRepository companhiaRepository;
	
	@BeforeEach
	public void setUp() {
		this.companhiaRepository = Mockito.mock(CompanhiaRepository.class);
		
		Companhia companhiaA = new Companhia("ComapanhiaA", new Pais("Argentina"));
		when(this.companhiaRepository.findById(1L)).thenReturn(Optional.of(companhiaA));
		when(this.companhiaRepository.findById(20L)).thenReturn(Optional.empty());
		
		this.validator = new CompanhiaExistenteValidator(companhiaRepository);
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarVooCOMCompanhiaQueNaoExiste() {
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(20L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("Companhia não existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarVooComCompanhiaQueExiste() {
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(1L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
}
