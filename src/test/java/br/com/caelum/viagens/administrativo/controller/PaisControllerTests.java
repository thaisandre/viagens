package br.com.caelum.viagens.administrativo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.viagens.administrativo.controller.dto.input.NewPaisInputDto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PaisControllerTests {
	
	private static final String ENDPOINT = "/paises";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PaisRepository paisRepository;
	
	private Pais brasil;
	
	@BeforeEach
	public void setUp() {
		brasil = this.paisRepository.save(new Pais("Brasil"));
	}
	
	@Test
	public void deveSalvarNovoPaisValidoQueAindaNaoExiste() throws Exception {
		
		NewPaisInputDto paisInputDto = new NewPaisInputDto();
		paisInputDto.setNome("Chile");
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(paisInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.nome").value("Chile"));	
	}
	
	@Test
	public void deveRejeitarCadastroDePaisSemNome() throws Exception {
		NewPaisInputDto paisInputDto = new NewPaisInputDto();
		paisInputDto.setNome("");
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(paisInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("nome"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode estar em branco"));
	}
	
	@Test
	public void deveRejeitarCadastroDePaisQueJaExiste() throws Exception {
		NewPaisInputDto paisInputDto = new NewPaisInputDto();
		paisInputDto.setNome("Brasil");
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(paisInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("nome"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("Pais já existe no sistema."));
	}
	
	@Test
	public void deveRetornarDetalhesDeUmPaisQueExiste() throws Exception {
		URI uri = new UriTemplate(ENDPOINT+"/{id}").expand(this.brasil.getId());
		 
		RequestBuilder request = get(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(this.brasil.getId()))
			.andExpect(jsonPath("$.nome").value("Brasil"));
	}
	
	@Test
	public void deveRetornarStatus404SeIdDoPaisNaoExistir() throws Exception {
		URI uri = new UriTemplate(ENDPOINT+"/{id}").expand("5");
		 
		RequestBuilder request = get(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		mockMvc.perform(request)
			.andExpect(status().isNotFound());
	}
}
