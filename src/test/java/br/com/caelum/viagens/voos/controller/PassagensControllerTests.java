package br.com.caelum.viagens.voos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;
import br.com.caelum.viagens.voos.model.RotaSemVoo;
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
	private RotaRepository rotaRepository;
	
	@Autowired
	private VooRepository vooRepository;

	private Voo voo;
	
	@BeforeEach
	public void setUp() {
		Pais argentina = new Pais("Argentina");
		Pais brasil = new Pais("Brasil");
		this.paisRepository.save(argentina);
		this.paisRepository.save(brasil);
		
		Aeroporto aeroportoA = new Aeroporto("AeroportoA", argentina);
		Aeroporto aeroportoB = new Aeroporto("AeroportoB", brasil);
		this.aeroportoRepository.save(aeroportoA);
		this.aeroportoRepository.save(aeroportoB);
		
		Rota rota = new Rota(aeroportoA, aeroportoB, 90);
		this.rotaRepository.save(rota);
		
		Set<RotaSemVoo> rotas = new HashSet<>();
		rotas.add(new RotaSemVoo(rota));
		
		Companhia companhiaA = new Companhia("CompanhiaA", argentina);
		this.companhiaRepository.save(companhiaA);
		
		this.voo = new Voo(rotas , companhiaA, 90);
		this.vooRepository.save(voo);
		
	}

	@Test
	public void deveSalvarNovaPassagemComDadosValidados() throws Exception {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		LocalDateTime dataEHora = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHora);
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		SimpleModule module = new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
	    ObjectMapper objectMapper = new ObjectMapper().registerModule(module);
	    
		RequestBuilder request = post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(objectMapper.writeValueAsString(passagemInputDto));
		
		List<br.com.caelum.viagens.voos.model.Rota> rotas 
			= this.voo.getRotasEmSequenciaLogica();
		
		mockMvc.perform(request).andExpect(status().isCreated())
			.andExpect(jsonPath("$.dataEHoraDePartida").value(dataEHora.format(formatter)))
			.andExpect(jsonPath("$.valor").value(900.00))
			.andExpect(jsonPath("$.voo.id").value(this.voo.getId()))
			.andExpect(jsonPath("$.voo.lugaresDisponiveis").value(this.voo.getLugaresDisponiveis()))
			.andExpect(jsonPath("$.voo.nomeCompanhia").value(this.voo.getNomeCompanhia()))
			.andExpect(jsonPath("$.voo.nomeDaOrigem").value(this.voo.getNomeDaOrigem()))
			.andExpect(jsonPath("$.voo.nomeDoDestino").value(this.voo.getNomeDoDestino()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].pernaFinal").value(rotas.get(0).isPernaFinal()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].origem.id").value(rotas.get(0).getOrigem().getId()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].origem.nome").value(rotas.get(0).getOrigem().getNome()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].origem.pais.id").value(rotas.get(0).getOrigem().getPais().getId()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].origem.pais.nome").value(rotas.get(0).getOrigem().getPais().getNome()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].destino.id").value(rotas.get(0).getDestino().getId()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].destino.nome").value(rotas.get(0).getDestino().getNome()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].destino.pais.id").value(rotas.get(0).getDestino().getPais().getId()))
			.andExpect(jsonPath("$.voo.rotasEmSequenciaLogica[0].destino.pais.nome").value(rotas.get(0).getDestino().getPais().getNome()));
		
	}
}
