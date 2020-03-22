package br.com.caelum.viagens.aeronaves.controller;

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

import br.com.caelum.viagens.aeronaves.controller.dto.input.NewAeronaveInputDto;
import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Modelo;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AeronavesControllerTests {
	
	private static final String ENDPOINT = "/aeronaves";
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AeronaveRepository aeronaveRepository;
	
	private Aeronave aeronave;
	
	@BeforeEach
	public void setUp() {
		this.aeronave = this.aeronaveRepository.save(new Aeronave(Modelo.ATR40));
	}
	
	@Test
	public void deveSalvarNovaAeronaveComModeloValido() throws Exception {
		NewAeronaveInputDto newAeronaveDto = new NewAeronaveInputDto();
		newAeronaveDto.setModelo(Modelo.ATR40);
		
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(newAeronaveDto));
		
		Aeronave aeronave  = newAeronaveDto.toModel();
		
		mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(jsonPath("$.modelo").value(aeronave.getModelo().toString()))
				.andExpect(jsonPath("$.assentos").isArray())
				.andExpect(jsonPath("$.assentos[*]").isNotEmpty())
				.andExpect(jsonPath("$.assentos[*].fileira").exists())
				.andExpect(jsonPath("$.assentos[*].posicao").exists());
	}
	
	@Test
	public void deveRetornarDetalhesDeUmaAeronaveQueExiste() throws Exception {
		URI uri = new UriTemplate(ENDPOINT+"/{id}").expand(this.aeronave.getId().toString());
		 
		RequestBuilder request = get(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(this.aeronave.getId()))
			.andExpect(jsonPath("$.modelo").value(this.aeronave.getModelo().toString()));
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
