package br.com.caelum.viagens.voos.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import br.com.caelum.viagens.voos.controller.dto.input.NewParadaDaRotaInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class RotaComTipoDeParadaExistenteValidatorTests {
	
	private RotaComTipoDeParadaExistenteValidator validator;
	
	@BeforeEach
	public void setUp() {
		this.validator = new RotaComTipoDeParadaExistenteValidator();
	}
	
	@Test
	public void deveDetectarErroQuandoCadastrarVooComTipoDeParadaQueNaoExiste() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		parada.setTipo("ESCALAA");
		
		NewRotaDoVooInputDto rota = new NewRotaDoVooInputDto();
		rota.setParada(parada);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isNotEmpty();
		assertThat(result.getGlobalErrors().get(0).getDefaultMessage())
			.isEqualTo("Tipo de parada \'ESCALAA\' é inválido.");
	}
	
	@Test
	public void naoDeveDetectarErroQuandoCadastrarVooComTiposDeParadasExistentes() {
		NewParadaDaRotaInputDto parada1 = new NewParadaDaRotaInputDto();
		parada1.setTipo("ESCALA");
		
		NewParadaDaRotaInputDto parada2 = new NewParadaDaRotaInputDto();
		parada2.setTipo("CONEXAO");
		
		NewRotaDoVooInputDto rota1 = new NewRotaDoVooInputDto();
		rota1.setParada(parada1);
		
		NewRotaDoVooInputDto rota2 = new NewRotaDoVooInputDto();
		rota2.setParada(parada2);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setRotas(rotas);
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(newVooDto, "newVooDto");
		validator.validate(newVooDto, result);
		
		assertThat(result.getGlobalErrors()).isEmpty();
	}
}
