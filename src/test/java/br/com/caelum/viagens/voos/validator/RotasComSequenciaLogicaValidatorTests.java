package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
		Aeroporto aeroportoE = new Aeroporto("AeroportoE", new Pais("Equador"));
		Aeroporto aeroportoP = new Aeroporto("AeroportoP", new Pais("Paraguai"));
		
		Rota rota1AtoB = new Rota(aeroportoA, aeroportoB, 120);
		Rota rota2BtoC = new Rota(aeroportoB, aeroportoC, 120);
		Rota rota3CtoU = new Rota(aeroportoC, aeroportoU, 120);
		Rota rota4CtoB = new Rota(aeroportoC, aeroportoB, 120);
		Rota rota5UtoA = new Rota(aeroportoU, aeroportoA, 120);
		Rota rota6UtoE = new Rota(aeroportoU, aeroportoE, 120);
		Rota rota7EtoP = new Rota(aeroportoE, aeroportoP, 120);
		Rota rota8CtoA = new Rota(aeroportoE, aeroportoP, 120);
		
		when(this.rotaRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(rota1AtoB));
		when(this.rotaRepository.findById(Long.valueOf(2L))).thenReturn(Optional.of(rota2BtoC));
		when(this.rotaRepository.findById(Long.valueOf(3L))).thenReturn(Optional.of(rota3CtoU));
		when(this.rotaRepository.findById(Long.valueOf(4L))).thenReturn(Optional.of(rota4CtoB));
		when(this.rotaRepository.findById(Long.valueOf(5L))).thenReturn(Optional.of(rota5UtoA));
		when(this.rotaRepository.findById(Long.valueOf(6L))).thenReturn(Optional.of(rota6UtoE));
		when(this.rotaRepository.findById(Long.valueOf(7L))).thenReturn(Optional.of(rota7EtoP));
		when(this.rotaRepository.findById(Long.valueOf(8L))).thenReturn(Optional.of(rota8CtoA));
		
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
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaCtoU);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
		
	}
	
	@Test
	public void deveCadastrarVooComRotasSemOrdemSequencial() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(1L);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(2L);
		
		NewRotaDoVooInputDto rotaUtoA = new NewRotaDoVooInputDto();
		rotaUtoA.setRotaId(5L);
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaUtoA);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
	
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComRotasComOrigemInicialIgualAoDestinoFinal() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(1L);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(2L);
		
		NewRotaDoVooInputDto rotaCtoA = new NewRotaDoVooInputDto();
		rotaCtoA.setRotaId(8L);
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaCtoA);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("rotas");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("as rotas não possuem uma sequência lógica.");
	}
	
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComRotasSemLigacao() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(1L);
		
		NewRotaDoVooInputDto rotaCtoU = new NewRotaDoVooInputDto();
		rotaCtoU.setRotaId(3L);
		
		NewRotaDoVooInputDto rotaCtoB = new NewRotaDoVooInputDto();
		rotaCtoB.setRotaId(4L);
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaCtoU);
		rotas.add(rotaCtoB);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("rotas");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("as rotas não possuem uma sequência lógica.");
	}
	
	@Test
	public void deveDetectarErrorNoCadastroDeVooComTrechoDeRotasDesconexas() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(1L);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(2L);
		
		NewRotaDoVooInputDto rotaUtoE = new NewRotaDoVooInputDto();
		rotaUtoE.setRotaId(6L);
		
		NewRotaDoVooInputDto rotaEtoP = new NewRotaDoVooInputDto();
		rotaEtoP.setRotaId(7L);
		
		Set<NewRotaDoVooInputDto> rotas = new HashSet<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaUtoE);
		rotas.add(rotaEtoP);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("rotas");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("as rotas não possuem uma sequência lógica.");
	}
	
}
