package br.com.caelum.viagens.administrativo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewAeroportoInputDto;
import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class AeroportosControllerTests {
	
	private static final String ENDPOINT = "/aeroportos";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private AeroportoRepository aeroportoRepository;

	private Pais argentina;
	private Pais brasil;
	private Aeroporto aeroportoA;
	
	@BeforeEach
	public void setUp() {
		this.argentina = this.paisRepository.save(new Pais("Argentina"));
		this.brasil = this.paisRepository.save(new Pais("Brasil"));
		this.aeroportoA = this.aeroportoRepository.save(new Aeroporto("AeroportoA", this.argentina));
	}
	
	@Test
	public void deveSalvarNovAeroportoValidoQueAindaNaoExiste() throws Exception {
		
		NewAeroportoInputDto aeroportoInputDto = new NewAeroportoInputDto();
		aeroportoInputDto.setNome("AeroportoB");
		aeroportoInputDto.setPaisId(this.argentina.getId());
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(aeroportoInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.nome").value("AeroportoB"))
			.andExpect(jsonPath("$.pais.id").value(argentina.getId()))
			.andExpect(jsonPath("$.pais.nome").value(argentina.getNome()));
	}
	
	@Test
	public void deveRejeitarCadastroDeAeroportoComNomeVazio() throws Exception {
		NewAeroportoInputDto aeroportoInputDto = new NewAeroportoInputDto();
		aeroportoInputDto.setNome("");
		aeroportoInputDto.setPaisId(this.brasil.getId());
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(aeroportoInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("nome"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode estar em branco"));
	}
	
	@Test
	public void deveRejeitarCadastroDeAeroportoComNomeQueJaExiste() throws Exception {
		
		NewAeroportoInputDto aeroportoInputDto = new NewAeroportoInputDto();
		aeroportoInputDto.setNome(this.aeroportoA.getNome());
		aeroportoInputDto.setPaisId(this.brasil.getId());
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(aeroportoInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("nome"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("Aeroporto de mesmo nome já existe no sistema."));
	}
	
	@Test
	public void deveRejeitarCadastroDeAeroportoSemUmPais() throws Exception {
		NewAeroportoInputDto aeroportoInputDto = new NewAeroportoInputDto();
		aeroportoInputDto.setNome("AeroportoB");
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(aeroportoInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("paisId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));
	}
	
	@Test
	public void deveRejeitarCadastroDeAeroportoComPaisQueNaoExiste() throws Exception {
		NewAeroportoInputDto aeroportoInputDto = new NewAeroportoInputDto();
		aeroportoInputDto.setNome("AeroportoB");
		aeroportoInputDto.setPaisId(20L);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(aeroportoInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("paisId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("País não existe no sistema."));
	}
	
	@Test
	public void deveRetornarDetalhesDeUmAeroportoQueExiste() throws Exception {
		 
		RequestBuilder request = get(ENDPOINT + "/" + this.aeroportoA.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(this.aeroportoA.getId()))
			.andExpect(jsonPath("$.nome").value(this.aeroportoA.getNome()))
			.andExpect(jsonPath("$.pais.id").value(this.argentina.getId()))
			.andExpect(jsonPath("$.pais.nome").value(this.argentina.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404SeIdDoAeroportoNaoExistir() throws Exception {
		
		RequestBuilder request = get(ENDPOINT + "/5")
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		mockMvc.perform(request)
			.andExpect(status().isNotFound());
	}
}
