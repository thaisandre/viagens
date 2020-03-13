package br.com.caelum.viagens.voos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;
import br.com.caelum.viagens.aeronaves.repository.AssentoRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;
import br.com.caelum.viagens.voos.controller.setup.CenariosPassagensControllerSetup;
import br.com.caelum.viagens.voos.model.Passagem;
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
	
	@Autowired
	private AssentoRepository assentoRepository;

	private CenariosPassagensControllerSetup cenarios;
	
	@BeforeEach
	public void setUp() {
		System.out.println("======" + this.aeroportoRepository);
		this.cenarios = new CenariosPassagensControllerSetup(paisRepository, companhiaRepository, 
				aeroportoRepository, rotaRepository, aeronaveRepository, vooRepository, assentoRepository);
		
	}

	@Test
	public void deveSalvarNovaPassagemComDadosValidados() throws Exception {
		NewPassagemInputDto newPassagemDto = cenarios.passagemValida();
		
		RequestBuilder request = processaRequest(newPassagemDto);
		
		Passagem passagem = newPassagemDto.toModel(vooRepository, assentoRepository);
		List<br.com.caelum.viagens.voos.model.Rota> rotas = passagem.getVoo().getRotasEmSequenciaLogica();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		
		mockMvc.perform(request).andExpect(status().isCreated())
			.andExpect(jsonPath("$.dataEHoraDePartida").value(passagem.getDataEHoraDePartida().format(formatter)))
			.andExpect(jsonPath("$.valor").value(900.00))
			.andExpect(jsonPath("$.assento.id").value(passagem.getAssento().getId()))
			.andExpect(jsonPath("$.rotas[0].pernaFinal").value(rotas.get(0).isPernaFinal()))
			.andExpect(jsonPath("$.rotas[0].origem.id").value(rotas.get(0).getOrigem().getId()))
			.andExpect(jsonPath("$.rotas[0].origem.nome").value(rotas.get(0).getOrigem().getNome()))
			.andExpect(jsonPath("$.rotas[0].origem.pais.id").value(rotas.get(0).getOrigem().getPais().getId()))
			.andExpect(jsonPath("$.rotas[0].origem.pais.nome").value(rotas.get(0).getOrigem().getPais().getNome()))
			.andExpect(jsonPath("$.rotas[0].destino.id").value(rotas.get(0).getDestino().getId()))
			.andExpect(jsonPath("$.rotas[0].destino.nome").value(rotas.get(0).getDestino().getNome()))
			.andExpect(jsonPath("$.rotas[0].destino.pais.id").value(rotas.get(0).getDestino().getPais().getId()))
			.andExpect(jsonPath("$.rotas[0].destino.pais.nome").value(rotas.get(0).getDestino().getPais().getNome()));
	}
	
	
	@Test
	public void naoDeveSalvarNovaPassagemComNoPassado() throws Exception {
		NewPassagemInputDto newPassagemDto = 
				cenarios.passagemComDataNoPassado();

		RequestBuilder request = processaRequest(newPassagemDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("dataEHoraDePartida"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve estar no futuro"));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooSemListaDeRotas() throws Exception {	
		NewPassagemInputDto newPassagemDto = cenarios.passagemComVooNaoExistente();

		RequestBuilder request = processaRequest(newPassagemDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("vooId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("vooId não existe no sistema."));		
	}
	
	@Test
	public void naoDeveSalvarNovaPassagemComValorNegativo() throws Exception {	
		NewPassagemInputDto newPassagemDto = cenarios.passagemComValorNegativo();

		RequestBuilder request = processaRequest(newPassagemDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("valor"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));		
	}
	
	@Test
	public void naoDeveSalvarNovaPassagemComValorIgualAZero() throws Exception {	
		NewPassagemInputDto newPassagemDto = cenarios.passagemComValorIgualAZero();

		RequestBuilder request = processaRequest(newPassagemDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("valor"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));		
	}
	
//	
//	@Test
//	public void naoDeveSalvarNovoVooComRotaComParadaComTempoIgualAZero() throws Exception {	
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaArgentinaParaBrasilComParadaComTempoIgualAZeroERotaBrasilParaChile();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())		
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas[].parada.tempo"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));		
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooComRotaComParadaComTempoNegativo() throws Exception {	
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaArgentinaParaBrasilComParadaComTempoNegativoERotaBrasilParaChile();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())		
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas[].parada.tempo"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));		
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooComRotaQueNaoExiste() throws Exception {	
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaInexistente();
//		
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())		
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("rotaId 1 não existe no sistema."));		
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooComRotasRepetidas() throws Exception {			
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotasDeArgentinaParaBrasilRepetidas();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())		
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não é permitido repetir rotas em um voo."));
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooComUmaUnicaRotaQueNaoEhPernalFinal() throws Exception {
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaArgentinaParaBrasilComParada();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("a lista de rotas precisa conter pelo menos uma rota final (sem parada)."));
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooComRotasQueNaoSaoPernalFinal() throws Exception {
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaChileComParada();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("a lista de rotas precisa conter pelo menos uma rota final (sem parada)."));
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooComMaisDeUmaRotaQueEhPernalFinal() throws Exception {
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaArgentinaParaBrasilSemParadaERotaBrasilParaChileSemParada();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("a lista de rotas precisa conter apenas uma única rota final (sem parada)."));
//	}
//	
//	@Test
//	public void naoDeveCadastrarVooComRotasComOrigemDeRotaAnteriorIgualAoDestinoDeRotaPosteriorConsecutiva() throws Exception {
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaArgentinaSemParada();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//		.andExpect(status().isBadRequest())
//		.andExpect(jsonPath("$.fieldErrors").isArray())
//		.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//		.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
//		
//	}
//	
//	
//	@Test
//	public void naoDeveSalvarNovoVooEmQueAOrigemDaRotaAnteriorSejaIgualAoDestinoDeUmaRotaPosteriorNaoConsecutiva() throws Exception {
//		
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaBrasilParaArgentinaComParadaERotaBrasilParaChileComParadaERotaChileParaArgentinaSemParada();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooEmQueUmRotaNaoPossuiConexaoComAsOutrasDuas() throws Exception {
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaChileComParadaERotaUruguaiParaParaguaiSemParada();
//
//		RequestBuilder request = processaRequest(newVooDto);
//		
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
//	}
//	
//	@Test
//	public void naoDeveSalvarNovoVooEmQueAsRotaPossuemRelacaoMasNaoUmaSequenciaLogica() throws Exception {
//		NewVooInputDto newVooDto = 
//				cenarios.vooComRotaBrasilParaArgentinaComParadaERotaBrasilParaChileComParadaERotaBrasilParaArgentinaSemParada();
//
//		RequestBuilder request = processaRequest(newVooDto);
//
//		mockMvc.perform(request)
//			.andExpect(status().isBadRequest())
//			.andExpect(jsonPath("$.fieldErrors").isArray())
//			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
//			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
//	}
	
	private MockHttpServletRequestBuilder processaRequest(NewPassagemInputDto newPassagemDto) throws JsonProcessingException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		SimpleModule module = new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
	    ObjectMapper objectMapper = new ObjectMapper().registerModule(module);
		
		return post(ENDPOINT).contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(objectMapper.writeValueAsString(newPassagemDto));
	}
	
	
	
	
}
