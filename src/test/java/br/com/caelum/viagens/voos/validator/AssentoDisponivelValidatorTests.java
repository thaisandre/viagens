package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;
import br.com.caelum.viagens.aeronaves.model.Modelo;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;
import br.com.caelum.viagens.voos.model.Passagem;
import br.com.caelum.viagens.voos.model.RotaSemVoo;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.PassagemRepository;

public class AssentoDisponivelValidatorTests {
	
	private AssentoDisponivelValidator validator;
	private PassagemRepository passagemRepository;
	
	@BeforeEach
	public void setUp() {
		this.passagemRepository = Mockito.mock(PassagemRepository.class);
		
		Pais argentina = new Pais("Argentina");
		Pais brasil = new Pais("Brasil");
		
		Aeroporto aeroportoA = new Aeroporto("AeroportoA", argentina);
		Aeroporto aeroportoB = new Aeroporto("AeroportoB", brasil);
		
		Companhia companhia = new Companhia("CompanhiaA", argentina);
		
		Rota rota = new Rota(aeroportoA, aeroportoB, 90);
		RotaSemVoo rotaSemVoo = new RotaSemVoo(rota );
		Set<RotaSemVoo> rotas = new HashSet<>();
		rotas.add(rotaSemVoo);
		
		Aeronave aeronave = new Aeronave(Modelo.ATR40);
		Assento assento = aeronave.getAssentos().stream().findFirst().get();
		
		Voo voo = new Voo(rotas, companhia, aeronave);
		Passagem passagem = new Passagem(voo, LocalDateTime.now().plusDays(2), new BigDecimal(500.0), assento);
		
		Aeronave aeronave2 = new Aeronave(Modelo.ATR40);
		Assento assento2 =  aeronave2.getAssentos().stream().findFirst().get();
		
		Voo voo2 = new Voo(rotas, companhia, aeronave2);
		Passagem passagem2 = new Passagem(voo2, LocalDateTime.now().plusDays(2), new BigDecimal(500.0), assento2);
		
		when(this.passagemRepository.findByVooIdAndAssentoId(1L, 1L)).thenReturn(Optional.of(passagem));
		when(this.passagemRepository.findByVooIdAndAssentoId(2L, 2L)).thenReturn(Optional.of(passagem2));
		
		this.validator = new AssentoDisponivelValidator(passagemRepository);
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarPassagemComAssentoDisponivel() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setVooId(1L);
		newPassagemDto.setAssentoId(2L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarPassagemComAssentoIndisponivel() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setVooId(1L);
		newPassagemDto.setAssentoId(1L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("assentoId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("assento não disponível.");
	}
}
