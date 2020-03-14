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
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;
import br.com.caelum.viagens.aeronaves.model.Modelo;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;
import br.com.caelum.viagens.voos.model.RotaSemVoo;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.VooRepository;

public class AssentoExistenteNoVooValidatorTests {
	
	private AssentoExistenteNoVooValidator validator;
	private VooRepository vooRepository;
	
	private Assento assento;
	
	@BeforeEach
	public void setUp() {
		this.vooRepository = Mockito.mock(VooRepository.class);
		
		Pais argentina = new Pais("Argentina");
		Pais brasil = new Pais("Brasil");
		
		Rota rota = new Rota(new Aeroporto("AeroportoA", argentina), new Aeroporto("AeroportoB", brasil), 90);
		Set<RotaSemVoo> rotas = new HashSet<>();
		rotas.add(new RotaSemVoo(rota));
		
		Companhia companhia = new Companhia("CompanhiaA", argentina);
		
		Aeronave aeronave = new Aeronave(Modelo.ATR40);
		this.assento = aeronave.getAssentos().stream().findFirst().get();
		
		Voo voo = new Voo(rotas, companhia, aeronave);
		
		when(this.vooRepository.findById(1L)).thenReturn(Optional.of(voo));
		
		this.validator = new AssentoExistenteNoVooValidator(vooRepository);
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarPassagemComAssentoExistenteNoVoo() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setVooId(1L);
		newPassagemDto.setAssentoId(this.assento.getId());
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isEmpty();
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarPassagemComAssentoQueNaoExisteNoVoo() {
		NewPassagemInputDto newPassagemDto = new NewPassagemInputDto();
		newPassagemDto.setVooId(1L);
		newPassagemDto.setAssentoId(20L);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newPassagemDto, "newPassagemDto");
		validator.validate(newPassagemDto, result);
		
		assertThat(result.getFieldErrors()).isNotEmpty();
		assertThat(result.getFieldErrors().get(0).getField()).isEqualTo("assentoId");
		assertThat(result.getFieldErrors().get(0).getDefaultMessage())
			.isEqualTo("assento não existe no voo.");
	}
}	
