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
		
		
		Aeronave aeronave = new Aeronave(Modelo.ATR40);
		Assento assento = new Assento(1, 'A', aeronave );
		
		Rota rota = new Rota(aeroportoA, aeroportoB, 90);
		RotaSemVoo rotaSemVoo = new RotaSemVoo(rota );
		Set<RotaSemVoo> rotas = new HashSet<>();
		rotas.add(rotaSemVoo);
		
		Voo voo = new Voo(rotas, new Companhia("CompanhiaA", new Pais("Argentina")), aeronave);
		Passagem passagem = new Passagem(voo, LocalDateTime.now().plusDays(2), new BigDecimal(500.0), assento);
		
		when(this.passagemRepository.findByAssentoId(1L)).thenReturn(Optional.of(passagem));
		when(this.passagemRepository.findByAssentoId(20L)).thenReturn(Optional.empty());
		
		this.validator = new AssentoDisponivelValidator(passagemRepository);
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarPassagemComAssentoDisponivel() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setAssentoId(20L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarPassagemComAssentoIndisponivel() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setAssentoId(1L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("assentoId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("assento não disponível.");
	}
}
