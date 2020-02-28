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

public class RotasComSequenciaLogicaValidatorTests {
	
	private RotasComSequenciaLogicaValidator validator;
	private RotaRepository rotaRepository;
	
	@BeforeEach
	public void setUp() {
		this.rotaRepository = Mockito.mock(RotaRepository.class);
		
		Aeroporto aeroportoA = new Aeroporto("AeroportoA", new Pais("Argentina"));
		Aeroporto aeroportoB = new Aeroporto("AeroportoB", new Pais("Brasil"));
		Aeroporto aeroportoC = new Aeroporto("AeroportoC", new Pais("Chile"));
		Aeroporto aeroportoU = new Aeroporto("AeroportoU", new Pais("Uruguai"));
		
		Rota rota1AtoB = new Rota(aeroportoA, aeroportoB, 120);
		Rota rota2BtoC = new Rota(aeroportoB, aeroportoC, 120);
		Rota rota3CtoU = new Rota(aeroportoC, aeroportoU, 120);
		Rota rota4CtoB = new Rota(aeroportoC, aeroportoB, 120);
		Rota rota5UtoA = new Rota(aeroportoU, aeroportoA, 120);
		
		when(this.rotaRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(rota1AtoB));
		when(this.rotaRepository.findById(Long.valueOf(2L))).thenReturn(Optional.of(rota2BtoC));
		when(this.rotaRepository.findById(Long.valueOf(3L))).thenReturn(Optional.of(rota3CtoU));
		when(this.rotaRepository.findById(Long.valueOf(4L))).thenReturn(Optional.of(rota4CtoB));
		when(this.rotaRepository.findById(Long.valueOf(5L))).thenReturn(Optional.of(rota5UtoA));
		
		this.validator = new RotasComSequenciaLogicaValidator(rotaRepository);
	}
	
	@Test
	public void deveCadastrarVooComRotasEmSequenciaLogica() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(1L);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(2L);
		
		NewRotaDoVooInputDto rotaCtoU = new NewRotaDoVooInputDto();
		rotaCtoU.setRotaId(3L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaCtoU);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isEmpty();
		
	}
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComRotasSemLigacao() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(1L);
		
		NewRotaDoVooInputDto rotaCtoU = new NewRotaDoVooInputDto();
		rotaCtoU.setRotaId(3L);
		
		NewRotaDoVooInputDto rotaCtoB = new NewRotaDoVooInputDto();
		rotaCtoB.setRotaId(4L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaCtoU);
		rotas.add(rotaCtoB);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("As rotas não possuem uma sequência lógica.");
		
	}
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComRotasSemOrdemSequencial() {
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(2L);
		
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(1L);
		
		NewRotaDoVooInputDto rotaUtoA = new NewRotaDoVooInputDto();
		rotaUtoA.setRotaId(5L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaBtoC);
		rotas.add(rotaAtoB);
		rotas.add(rotaUtoA);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
	
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("As rotas não possuem uma sequência lógica.");
		
	}
}
