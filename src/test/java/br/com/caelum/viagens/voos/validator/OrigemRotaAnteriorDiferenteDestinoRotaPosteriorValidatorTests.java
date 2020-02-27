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

import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;
import br.com.caelum.viagens.voos.validator.setup.ListaDeRotasSetUp;

public class OrigemRotaAnteriorDiferenteDestinoRotaPosteriorValidatorTests {
	
	private OrigemRotaAnteriorDiferenteDestinoRotaPosteriorValidator validator;
	private RotaRepository rotaRepository;
	
	@BeforeEach
	public void setUp() {
		rotaRepository = Mockito.mock(RotaRepository.class);
		
		List<Rota> rotas = ListaDeRotasSetUp.populaRotas();
		
		for(int i = 0; i < rotas.size(); i++) {
			when(this.rotaRepository.findById(Long.valueOf(i+1))).thenReturn(Optional.of(rotas.get(i)));
		}
		
		this.validator = new OrigemRotaAnteriorDiferenteDestinoRotaPosteriorValidator(rotaRepository);
	}
	
	@Test
	public void naoDeveCadastrarVooComRotasComOrigemDeRotaAnteriorIgualAoDestinoDeRotaPosteriorConsecutiva() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(4L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("A origem de uma rota anterior não pode ser igual ao destino de uma rota posterior.");
		
	}
	
	@Test
	public void naoDeveCadastrarVooComRotasComOrigemDeRotaAnteriorIgualAoDestinoDeRotaPosteriorNaoConsecutivas() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(9L);
		
		NewRotaDoVooInputDto rota3 = new NewRotaDoVooInputDto();
		rota2.setRotaId(10L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
		rotas.add(rota3);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("A origem de uma rota anterior não pode ser igual ao destino de uma rota posterior.");
		
	}
	
	@Test
	public void naoDeveCadastrarVooComRotasComOrigemDaprimeiraRotaIgualAoDestinoDaUltimaRotaNaoConsecutivas() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(9L);
		
		NewRotaDoVooInputDto rota3 = new NewRotaDoVooInputDto();
		rota3.setRotaId(12L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
		rotas.add(rota3);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("A origem de uma rota anterior não pode ser igual ao destino de uma rota posterior.");
		
	}
	
	@Test
	public void deveCadastrarVooComRotasComOrigemDeRotaanteriorDiferenteDeDestinoDeRotaPosterior() {
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setRotaId(1L);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setRotaId(5L);
		
		NewRotaDoVooInputDto rota3 = new NewRotaDoVooInputDto();
		rota3.setRotaId(9L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
		rotas.add(rota3);
				
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isEmpty();
		
	}
}
