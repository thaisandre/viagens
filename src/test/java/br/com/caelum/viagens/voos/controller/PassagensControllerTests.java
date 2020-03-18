package br.com.caelum.viagens.voos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;
import br.com.caelum.viagens.voos.controller.dto.input.UpdateInputPassagemDto;
import br.com.caelum.viagens.voos.controller.setup.CenariosPassagensControllerSetup;
import br.com.caelum.viagens.voos.model.Passagem;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.VooRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PassagensControllerTests {
	
	private static final String ENDPOINT = "/passagens";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private CompanhiaRepository companhiaRepository;

	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	@Autowired
	private AeronaveRepository aeronaveRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
	@Autowired
	private VooRepository vooRepository;
	
	private CenariosPassagensControllerSetup cenarios;
	
	@BeforeEach
	public void setUp() {
		this.cenarios = new CenariosPassagensControllerSetup(paisRepository, companhiaRepository, 
				aeroportoRepository, rotaRepository, aeronaveRepository, vooRepository);
		
	}

	@Test
	public void deveAtualizarNovaPassagemComValorPositivo() throws Exception {
		UpdateInputPassagemDto updatePassagemDto = cenarios.passagemComValorValido();
		
		List<Voo> voos = (List<Voo>) this.vooRepository.findAll();
		Passagem passagem = voos.get(0).getPassagens().stream().findFirst().get();
		
		System.out.println(passagem.getAssento());
		
		URI uri = new UriTemplate(ENDPOINT+"/{id}").expand(passagem.getId());
		RequestBuilder request = processaRequest(updatePassagemDto, uri);
		
		mockMvc.perform(request).andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(passagem.getId()))
			.andExpect(jsonPath("$.assento.id").value(passagem.getAssento().getId()))
			.andExpect(jsonPath("$.assento.fileira").value(passagem.getAssento().getFileira().toString()))
			.andExpect(jsonPath("$.assento.posicao").value(passagem.getAssento().getPosicao().toString()))
			.andExpect(jsonPath("$.valor").value(passagem.getValor()));
	}
	
	@Test
	public void naoDeveAtualizarNovaPassagemComValorNulo() throws Exception {
		UpdateInputPassagemDto updatePassagemDto = cenarios.passagemComValorNulo();
		
		List<Voo> voos = (List<Voo>) this.vooRepository.findAll();
		Passagem passagem = voos.get(0).getPassagens().stream().findFirst().get();
		
		URI uri = new UriTemplate(ENDPOINT+"/{id}").expand(passagem.getId());
		RequestBuilder request = processaRequest(updatePassagemDto, uri);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("valor"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));
	}
	
	@Test
	public void naoDeveAtualizarNovaPassagemComValorIgualAZero() throws Exception {
		UpdateInputPassagemDto updatePassagemDto = cenarios.passagemComValorIgualAZero();
		
		List<Voo> voos = (List<Voo>) this.vooRepository.findAll();
		Passagem passagem = voos.get(0).getPassagens().stream().findFirst().get();
		
		URI uri = new UriTemplate(ENDPOINT+"/{id}").expand(passagem.getId());
		RequestBuilder request = processaRequest(updatePassagemDto, uri);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("valor"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));
	}
	
	@Test
	public void naoDeveAtualizarNovaPassagemComValorNegativo() throws Exception {
		UpdateInputPassagemDto updatePassagemDto = cenarios.passagemComValorNegativo();
		
		List<Voo> voos = (List<Voo>) this.vooRepository.findAll();
		Passagem passagem = voos.get(0).getPassagens().stream().findFirst().get();
		
		URI uri = new UriTemplate(ENDPOINT+"/{id}").expand(passagem.getId());
		RequestBuilder request = processaRequest(updatePassagemDto, uri);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("valor"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));
	}
	
	private MockHttpServletRequestBuilder processaRequest(UpdateInputPassagemDto updatePassagemDto, URI uri) throws JsonProcessingException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm");
		SimpleModule module = new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
		ObjectMapper objectMapper = new ObjectMapper().registerModule(module);
	
		return put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(objectMapper.writeValueAsString(updatePassagemDto));
	}
	
}
