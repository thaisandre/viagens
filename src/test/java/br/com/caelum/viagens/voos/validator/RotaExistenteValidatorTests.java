package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaExistenteValidatorTests {
	
	private RotaExistenteValidator validator;
	private RotaRepository rotaRepository;
	
	@BeforeEach
	public void setUp() {
		rotaRepository = Mockito.mock(RotaRepository.class);
		
		Rota rota = new Rota(new Aeroporto("AeroportoA", new Pais("Argentina")), 
				new Aeroporto("AeroportoB", new Pais("Brasil")), 100);
		when(rotaRepository.findById(1L)).thenReturn(Optional.of(rota ));
		
		this.validator = new RotaExistenteValidator(rotaRepository);
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarComRotaQueNaoExiste() {
		NewRotaDoVooInputDto rota = new NewRotaDoVooInputDto();
		rota.setRotaId(10L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("rotaId não existe no sistema.");
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarVooComRotaQueExiste() {
		NewRotaDoVooInputDto rota = new NewRotaDoVooInputDto();
		rota.setRotaId(1L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}

}
