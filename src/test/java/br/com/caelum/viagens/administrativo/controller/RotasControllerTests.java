package br.com.caelum.viagens.administrativo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewRotaInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class RotasControllerTests {
	
	private static final String ENDPOINT = "/rotas";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	private Pais argentina;
	private Pais brasil;
	private Aeroporto aeroportoA;
	private Aeroporto aeroportoB;
	
	@BeforeEach
	public void setUp() {
		this.argentina = this.paisRepository.save(new Pais("Argentina"));
		this.brasil = this.paisRepository.save(new Pais("Brasil"));
		this.aeroportoA = this.aeroportoRepository.save(new Aeroporto("AeroportoA", this.argentina));
		this.aeroportoB = this.aeroportoRepository.save(new Aeroporto("AeroportoB", this.brasil));
	}
	
	@Test
	public void deveSalvarNovaRotaValidaQueAindaNaoExiste() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(this.aeroportoA.getId());
		rotaInputDto.setDestinoId(this.aeroportoB.getId());
		rotaInputDto.setDuracao(90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.nome").value("AeroportoA-AeroportoB"))
			.andExpect(jsonPath("$.origem.id").value(aeroportoA.getId()))
			.andExpect(jsonPath("$.origem.nome").value(aeroportoA.getNome()))
			.andExpect(jsonPath("$.origem.pais.id").value(aeroportoA.getPais().getId()))
			.andExpect(jsonPath("$.origem.pais.nome").value(aeroportoA.getPais().getNome()))
			.andExpect(jsonPath("$.destino.id").value(aeroportoB.getId()))
			.andExpect(jsonPath("$.destino.nome").value(aeroportoB.getNome()))
			.andExpect(jsonPath("$.destino.pais.id").value(aeroportoB.getPais().getId()))
			.andExpect(jsonPath("$.destino.pais.nome").value(aeroportoB.getPais().getNome()))
			.andExpect(jsonPath("$.duracao").value(90));
	}
	
	@Test
	public void naoDeveSalvarNovaRotaComOrigemNula() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setDestinoId(this.aeroportoA.getId());
		rotaInputDto.setDuracao(90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("origemId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));
	}
	
	
	@Test
	public void naoDeveSalvarNovaRotaComDestinoNulo() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(this.aeroportoA.getId());
		rotaInputDto.setDuracao(90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("destinoId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));
	}
	
	@Test
	public void naoDeveSalvarNovaRotaComDuracaoNula() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(this.aeroportoA.getId());
		rotaInputDto.setDestinoId(this.aeroportoB.getId());
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("duracao"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));
	}
	
	
	@Test
	public void naoDeveSalvarNovaRotaComDuracaoIgualAZero() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(this.aeroportoA.getId());
		rotaInputDto.setDestinoId(this.aeroportoB.getId());
		rotaInputDto.setDuracao(0);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("duracao"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));
	}
	
	@Test
	public void naoDeveSalvarNovaRotaComDuracaoNegativa() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(this.aeroportoA.getId());
		rotaInputDto.setDestinoId(this.aeroportoB.getId());
		rotaInputDto.setDuracao(-90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("duracao"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));
	}
	
	
	
	@Test
	public void naoDeveSalvarNovaRotaComOrigemEDestinoIguais() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(this.aeroportoA.getId());
		rotaInputDto.setDestinoId(this.aeroportoA.getId());
		rotaInputDto.setDuracao(90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.globalErrors").isArray())			
			.andExpect(jsonPath("$.globalErrors[*]").value("Aeroportos de origem e destino não podem ser iguais."));
	}
	
	@Test
	public void naoDeveSalvarNovaRotaComOrigemNaoExistente() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(20L);
		rotaInputDto.setDestinoId(this.aeroportoB.getId());
		rotaInputDto.setDuracao(90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("origemId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("Aeroporto origem não existe no sistema."));
	}
	
	@Test
	public void naoDeveSalvarNovaRotaComDestinoNaoExistente() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(this.aeroportoA.getId());
		rotaInputDto.setDestinoId(20L);
		rotaInputDto.setDuracao(90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("destinoId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("Aeroporto destino não existe no sistema."));
	}
	
	@Test
	public void naoDeveSalvarNovaRotaComOrigemEDestinoNaoExistentes() throws Exception {
		
		NewRotaInputDto rotaInputDto = new NewRotaInputDto();
		rotaInputDto.setOrigemId(21L);
		rotaInputDto.setDestinoId(20L);
		rotaInputDto.setDuracao(90);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(rotaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[0].campo").value("origemId"))
			.andExpect(jsonPath("$.fieldErrors[0].mensagem").value("Aeroporto origem não existe no sistema."))
			.andExpect(jsonPath("$.fieldErrors[1].campo").value("destinoId"))
			.andExpect(jsonPath("$.fieldErrors[1].mensagem").value("Aeroporto destino não existe no sistema."));
	}
	
	
}
