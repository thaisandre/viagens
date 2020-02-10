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

import br.com.caelum.viagens.administrativo.controller.dto.input.NewCompanhiaInputDto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CompanhiasControllerTests {
	
	private static final String ENDPOINT = "/companhias";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private CompanhiaRepository companhiaRepository;

	private Pais argentina;
	private Pais brasil;
	private Companhia companhiaA;
	
	@BeforeEach
	public void setUp() {
		this.argentina = this.paisRepository.save(new Pais("Argentina"));
		this.brasil = this.paisRepository.save(new Pais("Brasil"));
		this.companhiaA = this.companhiaRepository.save(new Companhia("CompanhiaA", this.argentina));
	}
	
	@Test
	public void deveSalvarNovaCompanhiaValidaQueAindaNaoExiste() throws Exception {
		
		NewCompanhiaInputDto companhiaInputDto = new NewCompanhiaInputDto();
		companhiaInputDto.setNome("CompanhiaB");
		companhiaInputDto.setPaisId(this.argentina.getId());
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(companhiaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.nome").value("CompanhiaB"))
			.andExpect(jsonPath("$.pais.id").value(argentina.getId()))
			.andExpect(jsonPath("$.pais.nome").value(argentina.getNome()));
	}
	
	@Test
	public void deveRejeitarCadastroDeCompanhiaComNomeVazio() throws Exception {
		NewCompanhiaInputDto companhiaInputDto = new NewCompanhiaInputDto();
		companhiaInputDto.setNome("");
		companhiaInputDto.setPaisId(brasil.getId());
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(companhiaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("nome"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode estar em branco"));
	}
	
	@Test
	public void deveRejeitarCadastroDeCompanhiaComNomeQueJaExiste() throws Exception {
		NewCompanhiaInputDto companhiaInputDto = new NewCompanhiaInputDto();
		companhiaInputDto.setNome(this.companhiaA.getNome());
		companhiaInputDto.setPaisId(this.brasil.getId());
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(companhiaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("nome"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("Companhia de mesmo nome já existe no sistema."));
	}
	
	@Test
	public void deveRejeitarCadastroDeCompanhiaSemUmPais() throws Exception {
		NewCompanhiaInputDto companhiaInputDto = new NewCompanhiaInputDto();
		companhiaInputDto.setNome("CompanhiaB");
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(companhiaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("paisId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));
	}
	
	@Test
	public void deveRejeitarCadastroDeCompanhiaComPaisQueNaoExiste() throws Exception {
		NewCompanhiaInputDto companhiaInputDto = new NewCompanhiaInputDto();
		companhiaInputDto.setNome("CompanhiaB");
		companhiaInputDto.setPaisId(20L);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(companhiaInputDto));
				
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("paisId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("País não existe no sistema."));
	}
	
	public void deveRetornarDetalhesDeUmaCompanhiaQueExiste() throws Exception {
		URI uri = new UriTemplate(ENDPOINT).expand(this.companhiaA.getId().toString());
		 
		RequestBuilder request = get(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(this.companhiaA.getId()))
			.andExpect(jsonPath("$.nome").value(this.companhiaA.getNome()))
			.andExpect(jsonPath("$.pais.id").value(this.argentina.getId()))
			.andExpect(jsonPath("$.pais.nome").value(this.argentina.getNome()));
	}
	
	public void deveRetornarStatus404SeIdDaCompanhiaNaoExistir() throws Exception {
		URI uri = new UriTemplate("/paises").expand("5");
		
		RequestBuilder request = get(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		mockMvc.perform(request)
			.andExpect(status().isNotFound());
	}
}
